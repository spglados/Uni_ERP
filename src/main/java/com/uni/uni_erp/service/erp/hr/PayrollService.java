package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Allowance;
import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Payroll;
import com.uni.uni_erp.dto.erp.hr.PayrollDTO;
import com.uni.uni_erp.exception.errorsRest.RestException400;
import com.uni.uni_erp.repository.erp.hr.AllowanceRepository;
import com.uni.uni_erp.repository.erp.hr.AttendanceRepository;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.repository.erp.hr.PayrollRepository;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final AttendanceRepository attendanceRepository;
    private final PayrollRepository payrollRepository;
    private final AllowanceRepository allowanceRepository;

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
            int totalOvertimeMinutes = 0;
            int totalHolidayWorkMinutes = 0;
            int totalNightWorkMinutes = 0;

            for (Attendance attendance : attendances) {
                if (attendance.getWorkTime() != null) {
                    totalWorkMinutes += attendance.getWorkTime();

                    // 추가 수당 계산
                    if (options.isIncludeOvertime()) {
                        totalOvertimeMinutes += calculateOvertime(attendance);
                    }
                    if (options.isIncludeHolidayWork()) {
                        totalHolidayWorkMinutes += calculateHolidayWork(attendance);
                    }
                    if (options.isIncludeNightWork()) {
                        totalNightWorkMinutes += calculateNightWork(attendance);
                    }
                }
            }

//            // 주휴 수당 계산
//            int numberOfWeeks = previousMonth.lengthOfMonth() / 7 + 1;
//            int weeklyWorkMinutes = totalWorkMinutes / numberOfWeeks;
//            Integer weeklyHolidayAllowance = 0;
//            if (weeklyWorkMinutes >= 15 * 60) { // 예: 주 15시간 이상 근무 시 주휴 수당
//                weeklyHolidayAllowance = calculateWeeklyHolidayAllowance(baseSalary);
//            }

            // 추가 수당 계산
            Integer overtimeAllowance = 0;
            Integer holidayWorkAllowance = 0;
            Integer nightWorkAllowance = 0;

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

    private int calculateOvertime(Attendance attendance) {
        // 예: 기본 근무 시간이 8시간을 초과한 경우 초과 근무 시간 반환
        int standardMinutes = 8 * 60;
        if (attendance.getWorkTime() > standardMinutes) {
            return attendance.getWorkTime() - standardMinutes;
        }
        return 0;
    }

    private int calculateHolidayWork(Attendance attendance) {
        // 휴일 근무 여부 판단 로직 추가
        // 예: 출근 날짜가 법정 휴일인지 확인
        // 법정 휴일인 경우 근무 시간 반환
        // 현재 예시에서는 휴일 여부 판단 로직이 없으므로 0 반환
        return 0;
    }

    private int calculateNightWork(Attendance attendance) {
        // 야간 근무 시간 계산 로직 추가
        // 예: 오후 10시 ~ 오전 6시 사이에 근무한 시간 반환
        // 현재 예시에서는 야간 근무 시간 계산 로직이 없으므로 0 반환
        return 0;
    }

    private Integer calculateWeeklyHolidayAllowance(Integer baseSalary) {
        // 주휴 수당 계산 로직
        // 예: 기본 시급의 일정 비율로 주휴 수당 계산
        // 예: 주휴 수당 = 기본 시급 * 8시간
        // 실제 계산 로직은 법정 기준에 따름
        return (int) (baseSalary / 160.0 * 8); // 예시: 월 160시간 기준
    }

}
