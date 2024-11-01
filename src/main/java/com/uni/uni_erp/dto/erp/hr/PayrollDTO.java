package com.uni.uni_erp.dto.erp.hr;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class PayrollDTO {

    @Data
    public static class CalculateDTO {
        private List<Long> empNos;
        private boolean includeOvertime;
        private boolean includeHolidayWork;
        private boolean includeNightWork;
        private boolean includeSundayWork;
        private boolean includeWeeklyHoliday;
    }

    @Data
    @Builder
    public static class CalculateResultDTO {
        private Long empNo;
        private String name;
        private String workSalary;
        private String overWorkAllowance;
        private String holidayWorkAllowance;
        private String nightWorkAllowance;
        private String weeklyHolidayAllowance;
    }

}
