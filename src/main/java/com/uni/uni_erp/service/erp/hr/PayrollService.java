package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Allowance;
import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.domain.entity.erp.hr.Holiday;
import com.uni.uni_erp.dto.erp.hr.PayrollDTO;
import com.uni.uni_erp.repository.erp.hr.AllowanceRepository;
import com.uni.uni_erp.repository.erp.hr.AttendanceRepository;
import com.uni.uni_erp.repository.erp.hr.HolidayRepository;
import com.uni.uni_erp.repository.erp.hr.PayrollRepository;
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
    private final HolidayService holidayService;
    private final HolidayRepository holidayRepository;

    @Transactional
    public void calculateGrossSalary(List<Long> employeeNos, PayrollDTO.CalculateDTO options) {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDate periodStart = previousMonth.atDay(1);
        LocalDate periodEnd = previousMonth.atEndOfMonth();

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
            int totalWorkMinutes = 0;

            for (Attendance attendance : attendances) {
                if (attendance.getWorkTime() != null) {
                    totalWorkMinutes += attendance.getWorkTime();
                }
            }
            // 초과 근무 시간 (연장 근무 수당 산출 용)
            int overtimeAllowance = 0;
            int totalHolidayWorkMinutes = 0;
            int totalNightWorkMinutes = 0;
            if (options.isIncludeOvertime()) {
                overtimeAllowance = (int) (calculateOvertime(empNo, periodStart, periodEnd, statuses) * 0.5);
            }
            if (options.isIncludeHolidayWork()) {
                totalHolidayWorkMinutes = calculateHolidayWork(empNo, periodStart, periodEnd, statuses, options.isIncludeSundayWork());
            }
            if (options.isIncludeNightWork()) {
                totalNightWorkMinutes = calculateNightWork(empNo, periodStart, periodEnd, statuses);
            }

//            // 주휴 수당 계산
//            int numberOfWeeks = previousMonth.lengthOfMonth() / 7 + 1;
//            int weeklyWorkMinutes = totalWorkMinutes / numberOfWeeks;
//            Integer weeklyHolidayAllowance = 0;
//            if (weeklyWorkMinutes >= 15 * 60) { // 예: 주 15시간 이상 근무 시 주휴 수당
//                weeklyHolidayAllowance = calculateWeeklyHolidayAllowance(baseSalary);
//            }

//            if (options.isIncludeOvertime()) {
//                overtimeAllowance = (int) ((totalOvertimeMinutes / 60.0) * employee.getWage() * 1.5);
//            }
//            if (options.isIncludeHolidayWork()) {
//                holidayWorkAllowance = (int) ((totalHolidayWorkMinutes / 60.0) * employee.getWage() * 1.5);
//            }
//            if (options.isIncludeNightWork()) {
//                nightWorkAllowance = (int) ((totalNightWorkMinutes / 60.0) * employee.getWage() * 1.5);
//            }

            // 세전 급여 계산

        }
    }

    /**
     * 초과 근무 수당 계산 기능
     *
     * @param empNo        사번
     * @param firstOfMonth 해당 달의 첫날
     * @param lastOfMonth  해당 달의 마지막 날
     * @param statuses     출근 상태 리스트
     * @return 초과 근무 수당
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
     * @return 휴일 근무 시간 (분)
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
     * @return 야간 근무 시간 (분)
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

    private Integer calculateWeeklyHolidayAllowance(Integer baseSalary) {
        // 주휴 수당 계산 로직
        // 예: 기본 시급의 일정 비율로 주휴 수당 계산
        // 예: 주휴 수당 = 기본 시급 * 8시간
        // 실제 계산 로직은 법정 기준에 따름
        return (int) (baseSalary / 160.0 * 8); // 예시: 월 160시간 기준
    }

}
