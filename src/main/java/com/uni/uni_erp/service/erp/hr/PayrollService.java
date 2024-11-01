package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.domain.entity.erp.hr.Holiday;
import com.uni.uni_erp.dto.erp.hr.PayrollDTO;
import com.uni.uni_erp.repository.erp.hr.*;
import com.uni.uni_erp.util.date.NumberFormatter;
import com.uni.uni_erp.util.define.Define_HR;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final AttendanceRepository attendanceRepository;
    private final PayrollRepository payrollRepository;
    private final AllowanceRepository allowanceRepository;
    private final EmployeeRepository employeeRepository;
    private final HolidayService holidayService;
    private final HolidayRepository holidayRepository;

    @Transactional
    public List<PayrollDTO.CalculateResultDTO> calculateGrossSalary(List<Long> employeeNos, PayrollDTO.CalculateDTO options) {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDate periodStart = previousMonth.atDay(1);
        LocalDate periodEnd = previousMonth.atEndOfMonth();
        List<PayrollDTO.CalculateResultDTO> resDTO = new ArrayList<>();
        for (Long empNo : employeeNos) {
            // 출근 기록 조회
            List<Attendance.Status> statuses = new ArrayList<>();
            statuses.add(Attendance.Status.LATE);
            statuses.add(Attendance.Status.LEFT_EARLY);
            statuses.add(Attendance.Status.ATTENDED);
            statuses.add(Attendance.Status.LATE_AND_LEFT_EARLY);
            statuses.add(Attendance.Status.UNPLANNED_WORK);
            List<Attendance> attendances = attendanceRepository.findByEmployeeNoAndDateBetween(empNo, periodStart, periodEnd, statuses);

            // 근무 시간 계산
            int totalWorkAllowance = 0;

            for (Attendance attendance : attendances) {
                if (attendance.getWorkTime() != null) {
                    totalWorkAllowance += attendance.getWorkTime() * (attendance.getWage() / 60);
                }
            }
            // 수당 계산
            int totalOverWorkAllowance = 0;
            int totalHolidayWorkAllowance = 0;
            int totalNightWorkAllowance = 0;
            int totalWeeklyHolidayAllowance = 0;
            if (options.isIncludeOvertime()) {
                totalOverWorkAllowance = (int) (calculateOvertime(empNo, periodStart, periodEnd, statuses) * 0.5);
            }
            if (options.isIncludeHolidayWork()) {
                totalHolidayWorkAllowance = (int) (calculateHolidayWork(empNo, periodStart, periodEnd, statuses, options.isIncludeSundayWork()) * 0.5);
            }
            if (options.isIncludeNightWork()) {
                totalNightWorkAllowance = (int) (calculateNightWork(empNo, periodStart, periodEnd, statuses) * 0.5);
            }
            if (options.isIncludeWeeklyHoliday()) {
                totalWeeklyHolidayAllowance = calculateWeeklyHolidayAllowance(empNo, periodStart, periodEnd, statuses);
            }
            resDTO.add(PayrollDTO.CalculateResultDTO.builder()
                            .empNo(empNo)
                            .name(attendances.get(0).getEmployee().getName())
                            .workSalary(NumberFormatter.formatToPrice(totalWorkAllowance))
                            .overWorkAllowance(NumberFormatter.formatToPrice(totalOverWorkAllowance))
                            .holidayWorkAllowance(NumberFormatter.formatToPrice(totalHolidayWorkAllowance))
                            .nightWorkAllowance(NumberFormatter.formatToPrice(totalNightWorkAllowance))
                            .weeklyHolidayAllowance(NumberFormatter.formatToPrice(totalWeeklyHolidayAllowance))
                    .build());
        }
        return resDTO;
    }

    /**
     * 초과 근무 수당 계산 기능
     *
     * @param empNo        사번
     * @param firstOfMonth 해당 달의 첫날
     * @param lastOfMonth  해당 달의 마지막 날
     * @param statuses     출근 상태 리스트
     * @return 초과 근무 수당 (100%) --> 비율 처리 필요
     */
    private int calculateOvertime(Long empNo, LocalDate firstOfMonth, LocalDate lastOfMonth, List<Attendance.Status> statuses) {
        // 주간 초과 근무 측정을 위해 전달의 마지막주 데이터까지 포함 시킴
        LocalDate start = firstOfMonth.with(DayOfWeek.SUNDAY);
        if (start.isAfter(firstOfMonth)) {
            start = start.minusWeeks(1);
        }
        int weeklyOverAllowance = 0;
        // 이달의 범위까지만
        while (!start.isAfter(lastOfMonth)) {
            // 일주일 범위 지정
            LocalDate end = start.plusDays(6);
            // 이달의 범위까지만
            if (end.isAfter(lastOfMonth)) {
                end = lastOfMonth;
            }
            // 일주일동안의 근무 내역 조회
            List<Attendance> attendances = attendanceRepository.findByEmployeeNoAndDateBetween(empNo, start, end, statuses);
            int weeklyTotalMinutes = 0;
            int weeklyDailyOverMinutes = 0;
            for (Attendance attendance : attendances) {
                // 주간 총 근무 시간 합계
                weeklyTotalMinutes += attendance.getWorkTime();
                // 일일 초과 근무 시간 계산 - 단, 이미 주간 초과 근무 시간을 초과한 근무는 중복 시키지 않음
                if (!attendance.getStartTime().toLocalDateTime().toLocalDate().isBefore(firstOfMonth) && weeklyTotalMinutes < Define_HR.WEEK_OVER_MINUTES) {
                    weeklyDailyOverMinutes += Math.max(attendance.getWorkTime() - Define_HR.DAY_OVER_MINUTES, 0);
                }
            }
            // 40시간을 초과한 시간과 일일 초과 근무 시간 총합 * 시급
            weeklyOverAllowance += (Math.max(weeklyTotalMinutes - (Define_HR.WEEK_OVER_MINUTES), 0) + weeklyDailyOverMinutes) * (attendances.get(attendances.size() - 1).getWage() / 60);
            start = start.plusDays(7);

        }

        return weeklyOverAllowance;
    }

    /**
     * 휴일 근무 계산 기능
     *
     * @param empNo        사번
     * @param firstOfMonth 해당 달의 첫날
     * @param lastOfMonth  해당 달의 마지막 날
     * @param statuses     출근 상태 리스트
     * @param sunday       일요일 포함 여부
     * @return 휴일 수당 (100%) --> 비율 처리 필요
     */
    private int calculateHolidayWork(Long empNo, LocalDate firstOfMonth, LocalDate lastOfMonth, List<Attendance.Status> statuses, boolean sunday) {
        List<Holiday> holidayList = holidayRepository.findByDateBetween(firstOfMonth, lastOfMonth);
        if (holidayList.isEmpty()) {
            holidayService.setHolidayByParsing(firstOfMonth);
            holidayService.setSunday(firstOfMonth);
        }
        if (sunday) {
            holidayList = holidayRepository.findByDateBetween(firstOfMonth, lastOfMonth);
        } else {
            holidayList = holidayRepository.findByDateBetweenAndType(firstOfMonth, lastOfMonth, Holiday.Type.HOLIDAY);
        }
        List<LocalDate> holidayDateList = holidayList.stream().map(Holiday::getDate).toList();
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeNoAndDateBetween(empNo, firstOfMonth, lastOfMonth, statuses);
        return attendanceList.stream()
                .filter(attendance -> holidayDateList.stream()
                        .anyMatch(holiday -> holiday.isEqual(attendance.getStartTime().toLocalDateTime().toLocalDate())))
                .mapToInt(attendance -> (attendance.getWorkTime() / 60) * attendance.getWage())
                .sum();
    }

    /**
     * 야간 근무 계산 기능
     *
     * @param empNo        사번
     * @param firstOfMonth 해당 달의 첫날
     * @param lastOfMonth  해당 달의 마지막 날
     * @param statuses     출근 상태 리스트
     * @return 야간 수당 (100%) --> 비율 처리 필요
     */
    private int calculateNightWork(Long empNo, LocalDate firstOfMonth, LocalDate lastOfMonth, List<Attendance.Status> statuses) {
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeNoAndDateBetween(empNo, firstOfMonth, lastOfMonth, statuses);

        // 야간 근무 시간대 정의
        LocalTime nightStart = LocalTime.of(22, 0);
        LocalTime nightEnd = LocalTime.of(6, 0);

        int totalNightWorkAllowance = 0;
        for (Attendance attendance : attendanceList) {
            LocalDateTime startTime = attendance.getStartTime().toLocalDateTime();
            LocalDateTime endTime = attendance.getEndTime().toLocalDateTime();
            int effectiveBreakTime = (attendance.getBreakTime() != null) ? attendance.getBreakTime() : 0;

            // 실제 근무 시간 추정
            int totalWorkMinutes = (int) Duration.between(startTime, endTime).toMinutes();
            int nightWorkMinutes = calculateOverlapWithNightShift(startTime.toLocalTime(), endTime.toLocalTime(), nightStart, nightEnd);

            // 전체 근무 시간에서 야간 근무 시간 비율 계산
            double nightWorkRatio = (double) nightWorkMinutes / totalWorkMinutes;

            // 총 휴식 시간의 일부를 야간 근무에 해당하는 시간으로 반영
            int nightBreakMinutes = (int) (effectiveBreakTime * nightWorkRatio);

            totalNightWorkAllowance += ((nightBreakMinutes - nightWorkMinutes) / 60) * attendance.getWage();
        }

        return totalNightWorkAllowance;
    }

    /**
     * 야간 근무 시간에 해당 하는 시간 계산
     *
     * @param workStart  근무 시작
     * @param workEnd    근무 끝
     * @param nightStart 야간 근무 시작
     * @param nightEnd   야간 근무 종료
     * @return 분 단위로 반환
     */
    private int calculateOverlapWithNightShift(LocalTime workStart, LocalTime workEnd, LocalTime nightStart, LocalTime nightEnd) {
        // 시작 시간이 밤 근무 시간에 포함될 경우 해당 시간부터 시작
        LocalTime overlapStart = workStart.isAfter(nightStart) || workStart.equals(nightStart) ? workStart : nightStart;
        LocalTime overlapEnd = workEnd.isBefore(nightEnd) || workEnd.equals(nightEnd) ? workEnd : nightEnd;

        // 겹치는 부분이 없을 경우
        if (overlapEnd.isBefore(overlapStart)) {
            return 0;
        }

        return (int) Duration.between(overlapStart, overlapEnd).toMinutes();
    }

    /**
     * 주휴 수당 계산 기능
     *
     * @param empNo        사번
     * @param firstOfMonth 해당 달의 첫날
     * @param lastOfMonth  해당 달의 마지막 날
     * @param statuses     출근 상태 리스트
     * @return 주휴 수당 (100%) --> 비율 처리 필요
     */
    private int calculateWeeklyHolidayAllowance(Long empNo, LocalDate firstOfMonth, LocalDate lastOfMonth, List<Attendance.Status> statuses) {
        // 주간 초과 근무 측정을 위해 전달의 마지막주 데이터까지 포함 시킴
        LocalDate start = firstOfMonth.with(DayOfWeek.SUNDAY);
        if (start.isAfter(firstOfMonth)) {
            start = start.minusWeeks(1);
        }
        // 주휴수당 미지급을 위해 결근 상태 확인
        statuses.add(Attendance.Status.UNAUTHORIZED_ABSENT);
        statuses.add(Attendance.Status.PERSONAL_ABSENT);
        int weeklyHolidayAllowance = 0;
        // 이달의 범위까지만
        while (!start.isAfter(lastOfMonth)) {
            // 일주일 범위 지정
            LocalDate end = start.plusDays(6);
            // 이달의 범위까지만
            if (end.isAfter(lastOfMonth)) {
                end = lastOfMonth;
            }
            // 일주일동안의 근무 내역 조회
            List<Attendance> attendances = attendanceRepository.findByEmployeeNoAndDateBetween(empNo, start, end, statuses);
            int weeklyTotalMinutesThisMonth = 0;
            int weeklyTotalMinutesLastMonth = 0;
            int weeklyCountThisMonth = 0;
            int weeklyCountLastMonth = 0;
            int weeklyTotalMinutes;
            int weeklyTotalCount;
            for (Attendance attendance : attendances) {

                // 주간 총 근무 시간 합계 - 전달과 이번달 분리
                if (attendance.getStartTime().toLocalDateTime().toLocalDate().isBefore(firstOfMonth)) {
                    weeklyTotalMinutesLastMonth += attendance.getWorkTime();
                    weeklyCountLastMonth++;
                } else {
                    weeklyTotalMinutesThisMonth += attendance.getWorkTime();
                    weeklyCountThisMonth++;
                    // 만약 무단 결근이나 개인사정 결근이 있을 경우 주휴수당 지급하지 않음
                    if (attendance.getStatus().equals(Attendance.Status.UNAUTHORIZED_ABSENT) || attendance.getStatus().equals(Attendance.Status.PERSONAL_ABSENT)) {
                        weeklyTotalMinutesThisMonth = 0;
                        weeklyTotalMinutesLastMonth = 0;
                        weeklyCountThisMonth = 0;
                        weeklyCountLastMonth = 0;
                        break;
                    }
                }
            }
            // 지난달의 근무 시간이 15시간이 넘는다면 지난달에 주휴수당을 지급했기때문에 이번달에 포함하지않음
            if (weeklyTotalMinutesLastMonth >= Define_HR.WEEKLY_HOLIDAY_MINUTES) {
                weeklyTotalMinutesLastMonth = 0;
                weeklyCountLastMonth = 0;
            }
            weeklyTotalMinutes = weeklyTotalMinutesThisMonth + weeklyTotalMinutesLastMonth;
            weeklyTotalCount = weeklyCountLastMonth + weeklyCountThisMonth;
            // 주간 일한 시간 / 일한 일수 --> 하루치 일한 시간
            if (weeklyTotalCount != 0) {
                weeklyHolidayAllowance += (weeklyTotalMinutes / weeklyTotalCount) * (attendances.get(attendances.size() - 1).getWage() / 60);
            }
            start = start.plusDays(7);

        }
        return weeklyHolidayAllowance;
    }

}
