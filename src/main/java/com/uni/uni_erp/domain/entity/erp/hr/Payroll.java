package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.converter.erp.hr.YearMonthConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.YearMonth;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "hr_payroll_tb")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @Column(name = "year_month", nullable = false)
    @Convert(converter = YearMonthConverter.class)
    private YearMonth yearMonth;

    @Column(name = "gross_salary", nullable = false)
    private Integer grossSalary; // 세전 급여

    @Column(name = "work_salary", nullable = false)
    private Integer workSalary;

    @Column(name = "total_work_time", nullable = false)
    private Integer totalWorkTime; // 시간

    @Column(name = "over_work_allowance", nullable = true)
    private Integer overWorkAllowance;

    @Column(name = "holiday_work_allowance", nullable = true)
    private Integer holidayWorkAllowance;

    @Column(name = "night_work_allowance", nullable = true)
    private Integer nightWorkAllowance;

    @Column(name = "weekly_holiday_allowance", nullable = true)
    private Integer weeklyHolidayAllowance;

    @Column(name = "national_pension", nullable = true)
    private Integer nationalPension;

    @Column(name = "health_insurance", nullable = true)
    private Integer healthInsurance;

    @Column(name = "employment_insuracne", nullable = true)
    private Integer employmentInsurance;

    @Column(name = "employment_insurance_employer", nullable = true)
    private Integer employmentInsuranceEmployer;

    @Column(name = "industrial_accident_compensation_insurance", nullable = true)
    private Integer industrialAccidentCompensationInsurance;

    @Column(name = "total_insurance", nullable = true)
    private Integer totalInsurance;

    @Column(name = "net_salary", nullable = false)
    private Integer netSalary; // 실수령액

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
