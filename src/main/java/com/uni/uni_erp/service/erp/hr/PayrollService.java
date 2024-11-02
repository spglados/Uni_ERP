package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.domain.entity.erp.hr.Holiday;
import com.uni.uni_erp.dto.erp.hr.EmployeeDTO;
import com.uni.uni_erp.dto.erp.hr.PayrollDTO;
import com.uni.uni_erp.repository.erp.hr.*;
import com.uni.uni_erp.util.date.NumberFormatter;
import com.uni.uni_erp.util.define.Define_HR;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
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

    public List<EmployeeDTO> getEmployeesNoCalculated(Integer storeId) {
        return payrollRepository.findEmployeesWithoutPayroll(storeId, YearMonth.now().minusYears(1))
                .stream()
                .map(EmployeeDTO::new)
                .toList();
    }

    /**
     * 해당 사번의 총 급여 및 각종 수당, 세금 계산
     *
     * @param employeeNos 사번 목록
     * @param options     수당 옵션
     * @return 모든 데이터 반환
     */
    @Transactional
    public List<PayrollDTO.CalculateResultDTO> calculateGrossSalary(List<Long> employeeNos, PayrollDTO.CalculateDTO options) {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDate periodStart = previousMonth.atDay(1);
        LocalDateTime periodStartTime = LocalDateTime.of(periodStart, LocalTime.of(6, 0));
        LocalDate periodEnd = previousMonth.atEndOfMonth().plusDays(1);
        LocalDateTime periodEndTime = LocalDateTime.of(periodEnd, LocalTime.of(5, 59));
        List<PayrollDTO.CalculateResultDTO> resDTO = new ArrayList<>();
        for (Long empNo : employeeNos) {
            // 출근 기록 조회
            List<Attendance.Status> statuses = new ArrayList<>();
            statuses.add(Attendance.Status.LATE);
            statuses.add(Attendance.Status.LEFT_EARLY);
            statuses.add(Attendance.Status.ATTENDED);
            statuses.add(Attendance.Status.LATE_AND_LEFT_EARLY);
            statuses.add(Attendance.Status.UNPLANNED_WORK);
            List<Attendance> attendances = attendanceRepository.findByEmployeeNoAndDateBetween(empNo, periodStartTime, periodEndTime, statuses);

            // 근무 시간 계산
            int totalWorkAllowance = 0;
            int totalWorkTime = 0;
            for (Attendance attendance : attendances) {
                if (attendance.getWorkTime() != null) {
                    totalWorkTime += attendance.getWorkTime();
                    totalWorkAllowance += attendance.getWorkTime() * (attendance.getWage() / 60);
                }
            }
            // 수당 계산
            int totalOverWorkAllowance = 0;
            int totalHolidayWorkAllowance = 0;
            int totalNightWorkAllowance = 0;
            int totalWeeklyHolidayAllowance = 0;
            if (options.isIncludeOvertime()) {
                totalOverWorkAllowance = (int) (calculateOvertime(empNo, periodStartTime, periodEndTime, statuses) * 0.5);
            }
            if (options.isIncludeHolidayWork()) {
                totalHolidayWorkAllowance = (int) (calculateHolidayWork(empNo, periodStartTime, periodEndTime, statuses, options.isIncludeSundayWork()) * 0.5);
            }
            if (options.isIncludeNightWork()) {
                totalNightWorkAllowance = (int) (calculateNightWork(empNo, periodStartTime, periodEndTime, statuses) * 0.5);
            }
            if (options.isIncludeWeeklyHoliday()) {
                totalWeeklyHolidayAllowance = calculateWeeklyHolidayAllowance(empNo, periodStartTime, periodEndTime, statuses);
            }
            // 세전 급여
            int grossSalary = totalWorkAllowance + totalOverWorkAllowance + totalHolidayWorkAllowance + totalNightWorkAllowance + totalWeeklyHolidayAllowance;
            // 세금 계산
            int nationalPension = (int) (Math.max(Math.min(grossSalary, Define_HR.NATIONAL_PENSION_MAX), Define_HR.NATIONAL_PENSION_MIN) * Define_HR.NATIONAL_PENSION);
            int healthInsurance = (int) (Math.max(Math.min(grossSalary, Define_HR.HEALTH_INSURANCE_MAX), Define_HR.HEALTH_INSURANCE_MIN) * Define_HR.HEALTH_INSURANCE);
            int employmentInsurance = (int) (grossSalary * Define_HR.EMPLOYMENT_INSURANCE);
            int employmentInsuranceEmployer = (int) (grossSalary * Define_HR.EMPLOYMENT_INSURANCE_EMPLOYER);
            int industrialAccidentCompensationInsurance = (int) (grossSalary * Define_HR.INDUSTRIAL_ACCIDENT_COMPENSATION_INSURANCE);
            resDTO.add(PayrollDTO.CalculateResultDTO.builder()
                    .empNo(empNo)
                    .name(attendances.get(0).getEmployee().getName())
                    .grossSalary(NumberFormatter.formatToPrice(grossSalary))
                    .workSalary(NumberFormatter.formatToPrice(totalWorkAllowance))
                    .overWorkAllowance(NumberFormatter.formatToPrice(totalOverWorkAllowance))
                    .holidayWorkAllowance(NumberFormatter.formatToPrice(totalHolidayWorkAllowance))
                    .nightWorkAllowance(NumberFormatter.formatToPrice(totalNightWorkAllowance))
                    .weeklyHolidayAllowance(NumberFormatter.formatToPrice(totalWeeklyHolidayAllowance))
                    .totalWorkTime(totalWorkTime / 60)
                    .nationalPension(NumberFormatter.formatToPrice(nationalPension))
                    .healthInsurance(NumberFormatter.formatToPrice(healthInsurance))
                    .employmentInsurance(NumberFormatter.formatToPrice(employmentInsurance))
                    .employmentInsuranceEmployer(NumberFormatter.formatToPrice(employmentInsuranceEmployer))
                    .industrialAccidentCompensationInsurance(NumberFormatter.formatToPrice(industrialAccidentCompensationInsurance))
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
    private int calculateOvertime(Long empNo, LocalDateTime firstOfMonth, LocalDateTime lastOfMonth, List<Attendance.Status> statuses) {
        // 주간 초과 근무 측정을 위해 전달의 마지막주 데이터까지 포함 시킴
        LocalDateTime start = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        if (start.isAfter(firstOfMonth)) {
            start = start.minusWeeks(1);
        }
        int weeklyOverAllowance = 0;
        // 이달의 범위까지만
        while (!start.isAfter(lastOfMonth)) {
            // 일주일 범위 지정
            LocalDateTime end = start.plusDays(7).minusMinutes(1);
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
                if (!attendance.getStartTime().toLocalDateTime().isBefore(firstOfMonth) && weeklyTotalMinutes < Define_HR.WEEK_OVER_MINUTES) {
                    weeklyDailyOverMinutes += Math.max(attendance.getWorkTime() - Define_HR.DAY_OVER_MINUTES, 0);
                }
            }
            // 40시간을 초과한 시간과 일일 초과 근무 시간 총합 * 시급
            if (!attendances.isEmpty()) {
                weeklyOverAllowance += (Math.max(weeklyTotalMinutes - (Define_HR.WEEK_OVER_MINUTES), 0) + weeklyDailyOverMinutes) * (attendances.get(attendances.size() - 1).getWage() / 60);
            }
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
    private int calculateHolidayWork(Long empNo, LocalDateTime firstOfMonth, LocalDateTime lastOfMonth, List<Attendance.Status> statuses, boolean sunday) {
        List<Holiday> holidayList = holidayRepository.findByDateBetween(firstOfMonth.toLocalDate(), lastOfMonth.toLocalDate().minusDays(1));
        if (holidayList.isEmpty()) {
            holidayService.setHolidayByParsing(firstOfMonth.toLocalDate());
            holidayService.setSunday(firstOfMonth.toLocalDate());
        }
        if (sunday) {
            holidayList = holidayRepository.findByDateBetween(firstOfMonth.toLocalDate(), lastOfMonth.toLocalDate().minusDays(1));
        } else {
            holidayList = holidayRepository.findByDateBetweenAndType(firstOfMonth.toLocalDate(), lastOfMonth.toLocalDate().minusDays(1), Holiday.Type.HOLIDAY);
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
    private int calculateNightWork(Long empNo, LocalDateTime firstOfMonth, LocalDateTime lastOfMonth, List<Attendance.Status> statuses) {
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeNoAndDateBetween(empNo, firstOfMonth, lastOfMonth, statuses);

        // 야간 근무 시간대 정의
        LocalTime nightStart = LocalTime.of(22, 0);

        int totalNightWorkAllowance = 0;
        for (Attendance attendance : attendanceList) {
            LocalDateTime startTime = attendance.getStartTime().toLocalDateTime();
            LocalDateTime endTime = attendance.getEndTime().toLocalDateTime();
            int effectiveBreakTime = (attendance.getBreakTime() != null) ? attendance.getBreakTime() : 0;

            // 실제 근무 시간 추정
            int totalWorkMinutes = (int) Duration.between(startTime, endTime).toMinutes();
            int nightWorkMinutes = calculateOverlapWithNightShift(startTime.toLocalTime(), endTime.toLocalTime(), nightStart);

            // 전체 근무 시간에서 야간 근무 시간 비율 계산
            double nightWorkRatio = (double) nightWorkMinutes / totalWorkMinutes;

            // 총 휴식 시간의 일부를 야간 근무에 해당하는 시간으로 반영
            int nightBreakMinutes = (int) (effectiveBreakTime * nightWorkRatio);

            totalNightWorkAllowance += (nightWorkMinutes - nightBreakMinutes) * (attendance.getWage() / 60);
        }

        return totalNightWorkAllowance;
    }

    /**
     * 야간 근무 시간에 해당 하는 시간 계산
     *
     * @param workStart  근무 시작
     * @param workEnd    근무 끝
     * @param nightStart 야간 근무 시작
     * @return 분 단위로 반환
     */
    private int calculateOverlapWithNightShift(LocalTime workStart, LocalTime workEnd, LocalTime nightStart) {
        LocalDate temp = LocalDate.now();
        // 야간 근무 시작과 종료 시간을 임시 날짜로 설정
        LocalDateTime startNightShift = LocalDateTime.of(temp, nightStart);
        LocalDateTime endNightShift = startNightShift.plusHours(8); // 22:00부터 익일 6:00까지 8시간

        // 근무 시작과 종료 시간을 같은 임시 날짜로 설정, 자정을 넘는 경우를 위해 workEnd에 하루 추가
        LocalDateTime actualWorkStart = LocalDateTime.of(temp, workStart);
        LocalDateTime actualWorkEnd = workEnd.isBefore(workStart) ? LocalDateTime.of(temp.plusDays(1), workEnd) : LocalDateTime.of(temp, workEnd);

        // 겹치는 시간이 없으면 0 반환
        if (actualWorkEnd.isBefore(startNightShift) || actualWorkStart.isAfter(endNightShift)) {
            return 0;
        }

        // 겹치는 시간 계산
        LocalDateTime overlapStart = actualWorkStart.isAfter(startNightShift) ? actualWorkStart : startNightShift;
        LocalDateTime overlapEnd = actualWorkEnd.isBefore(endNightShift) ? actualWorkEnd : endNightShift;

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
    private int calculateWeeklyHolidayAllowance(Long empNo, LocalDateTime firstOfMonth, LocalDateTime lastOfMonth, List<Attendance.Status> statuses) {
        // 주간 초과 근무 측정을 위해 전달의 마지막주 데이터까지 포함 시킴
        LocalDateTime start = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
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
            LocalDateTime end = start.plusDays(7).minusMinutes(1);
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
                if (attendance.getStartTime().toLocalDateTime().isBefore(firstOfMonth)) {
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
