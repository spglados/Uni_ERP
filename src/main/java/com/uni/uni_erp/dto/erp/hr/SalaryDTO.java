package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.domain.entity.erp.hr.Payroll;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryDTO {
    private Long empNo;
    private String name;
    private String phone;
    private String accountNumber;
    private Integer wage;
    private SalaryDetail salaryDetails;

    public SalaryDTO(Payroll payroll, List<Attendance> attendances) {
        this.empNo = payroll.getEmployee().getUniqueEmployeeNumber();
        this.name = payroll.getEmployee().getName();
        this.phone = payroll.getEmployee().getPhone();
        this.accountNumber = payroll.getEmployee().getAccountNumber();
        this.wage = payroll.getEmployee().getWage();
        this.salaryDetails = new SalaryDetail(payroll, attendances);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SalaryDetail {
        private int year;
        private int month;
        private LocalDate payDate;
        private Integer totalWorkHours;
        private Integer totalWorkDays;
        private Integer totalSalary;
        private Integer deductions;
        private Integer netPay;
        private List<DailyAttendance> dailyAttendances;

        public SalaryDetail(Payroll payroll, List<Attendance> attendances) {
            this.year = payroll.getYearMonth().getYear();
            this.month = payroll.getYearMonth().getMonthValue();
            this.payDate = payroll.getCreatedAt().toLocalDate();
            this.totalWorkHours = payroll.getTotalWorkTime();
            this.totalWorkDays = attendances.size();
            this.totalSalary = payroll.getGrossSalary();
            this.deductions = payroll.getTotalInsurance();
            this.netPay = payroll.getNetSalary();
            this.dailyAttendances = attendances.stream().map(DailyAttendance::new).toList();
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class DailyAttendance {
            private LocalDate date;
            private LocalTime clockIn;
            private LocalTime clockOut;
            private Boolean hasBreak;
            private String attendanceStatus; // NORMAL, EARLY_LEAVE, LATE

            public DailyAttendance(Attendance attendance) {
                this.date = attendance.getStartTime().toLocalDateTime().toLocalDate();
                this.clockIn = attendance.getStartTime().toLocalDateTime().toLocalTime();
                this.clockOut = attendance.getEndTime().toLocalDateTime().toLocalTime();
                this.hasBreak = attendance.getBreakTime() != null && attendance.getBreakTime() != 0;
                this.attendanceStatus = attendance.getStatus().getDescription();
            }
        }
    }
}