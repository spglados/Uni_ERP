package com.uni.uni_erp.domain.entity.erp.hr;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "hr_allowance_tb")
public class Allowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payroll;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllowanceType type;

    @Column(nullable = false)
    private Integer amount; // 수당 금액

    // 출처가 되는 Attendance 엔티티를 참조할 수도 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = true)
    private Attendance attendance;

    public enum AllowanceType {
        OVERTIME,       // 연장 근무
        HOLIDAY_WORK,   // 휴일 근무
        NIGHT_WORK,     // 야간 근무
        WEEKLY_REST,    // 주휴 수당
    }

}