package com.uni.uni_erp.dto.erp.hr;

import lombok.Data;

public class PayrollDTO {

    @Data
    public static class CalculateDTO {
        private boolean includeOvertime;
        private boolean includeHolidayWork;
        private boolean includeNightWork;
        private boolean includeSundayWork;
    }

}
