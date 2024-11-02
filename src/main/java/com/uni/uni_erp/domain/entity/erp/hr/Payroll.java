package com.uni.uni_erp.domain.entity.erp.hr;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

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

    @Column(name = "period_start", nullable = false)
    private Timestamp periodStart;

    @Column(name = "period_end", nullable = false)
    private Timestamp periodEnd;

    @Column(name = "gross_salary", nullable = false)
    private Integer grossSalary; // 세전 급여

    @Column(name = "tax", nullable = true)
    private Integer tax; // 세금

    @Column(name = "net_salary", nullable = false)
    private Integer netSalary; // 실수령액

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allowance> allowances;

}
