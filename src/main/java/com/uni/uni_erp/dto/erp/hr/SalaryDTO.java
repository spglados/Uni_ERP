package com.uni.uni_erp.dto.erp.hr;

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
    private Integer id;
    private String name;
    private String contact;
    private String salaryAccount;
    private Double hourlyRate;
    private SalaryDetail salaryDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SalaryDetail {
        private int year;
        private int month;
        private LocalDate payDate;
        private Double totalWorkHours;
        private Integer totalWorkDays;
        private Double totalSalary;
        private Double deductions;
        private Double netPay;
        private List<DailyAttendance> dailyAttendances;

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
            private Double dailyAmount;
            private String attendanceStatus; // NORMAL, EARLY_LEAVE, LATE
        }
    }
}