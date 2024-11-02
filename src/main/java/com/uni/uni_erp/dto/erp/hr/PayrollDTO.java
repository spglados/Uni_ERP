package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Payroll;
import com.uni.uni_erp.util.date.NumberFormatter;
import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;
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
        private boolean includeInsurance;
    }

    @Data
    @Builder
    public static class CalculateResultDTO {
        private Long empNo;
        private String name;
        private String grossSalary;
        private String workSalary;
        private String overWorkAllowance;
        private String holidayWorkAllowance;
        private String nightWorkAllowance;
        private String weeklyHolidayAllowance;
        private Integer totalWorkTime;
        private String nationalPension;
        private String healthInsurance;
        private String employmentInsurance;
        private String employmentInsuranceEmployer;
        private String industrialAccidentCompensationInsurance;
        private String totalInsurance;
        private String netSalary;

        public Payroll toEntity(Employee employee) {
            YearMonth yearMonth = YearMonth.now().minusMonths(1);
            return Payroll.builder()
                    .employee(employee)
                    .yearMonth(yearMonth)
                    .grossSalary(NumberFormatter.parseToPrice(grossSalary))
                    .workSalary(NumberFormatter.parseToPrice(workSalary))
                    .netSalary(NumberFormatter.parseToPrice(netSalary))
                    .totalWorkTime(totalWorkTime)
                    .overWorkAllowance(NumberFormatter.parseToPrice(overWorkAllowance))
                    .holidayWorkAllowance(NumberFormatter.parseToPrice(holidayWorkAllowance))
                    .nightWorkAllowance(NumberFormatter.parseToPrice(nightWorkAllowance))
                    .weeklyHolidayAllowance(NumberFormatter.parseToPrice(weeklyHolidayAllowance))
                    .nationalPension(NumberFormatter.parseToPrice(nationalPension))
                    .healthInsurance(NumberFormatter.parseToPrice(healthInsurance))
                    .employmentInsurance(NumberFormatter.parseToPrice(employmentInsurance))
                    .industrialAccidentCompensationInsurance(NumberFormatter.parseToPrice(industrialAccidentCompensationInsurance))
                    .employmentInsurance(NumberFormatter.parseToPrice(employmentInsurance))
                    .build();
        }
    }

    @Data
    public static class CreateDTO {
        private List<Long> empNos;
        private boolean includeOvertime;
        private boolean includeHolidayWork;
        private boolean includeNightWork;
        private boolean includeWeeklyHoliday;
        private boolean includeInsurance;
        private List<CalculateResultDTO> payrollData;
    }

}
