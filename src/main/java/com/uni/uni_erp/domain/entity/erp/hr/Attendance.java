package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "hr_attendance_tb")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @Column(name = "start_time", nullable = true)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = true)
    private Timestamp endTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Schedule.Status status = Schedule.Status.NOT_EXECUTED;

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
