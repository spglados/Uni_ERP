package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "attendance_tb")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 외래 키 설정: Employee와 다대일 관계 (하나의 직원은 여러 출석 기록을 가질 수 있음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "start_time", nullable = true)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = true)
    private Timestamp endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // 출석 상태

    // 출석 상태를 관리하는 enum
    @RequiredArgsConstructor
    @Getter
    public enum Status {
        NOT_EXECUTED("이행되지 않음"),
        WORKING("근무중"),
        ATTENDED("출석"),
        LATE("지각"),
        LEFT_EARLY("조퇴"),
        ABSENT("결근");

        private final String description;
    }
}
