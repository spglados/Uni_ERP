package com.uni.uni_erp.domain.entity.erp.hr;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Entity
@Table(name = "attendance_tb")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    // 외래 키 설정: Employee와 다대일 관계 (하나의 직원은 여러 출석 기록을 가질 수 있음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Timestamp date; // 날짜 (연월일)

    @Column(nullable = true)
    private Timestamp checkIn; // 출근 시간

    @Column(nullable = true)
    private Timestamp checkOut; // 퇴근 시간

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // 출석 상태

    // 출석 상태를 관리하는 enum
    public enum Status {
        PRESENT,    // 출석
        ABSENT,     // 결근
        LATE,       // 지각
        ON_LEAVE    // 휴가
    }
}
