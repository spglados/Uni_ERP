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
@Table(name = "hr_schedule_tb")
public class Schedule {

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.NOT_EXECUTED;

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        NOT_EXECUTED("이행되지 않음"),
        LATE("지각"),
        LEFT_EARLY("조퇴"),
        ATTENDED("출근");

        private final String description;
    }

    // 지각이나 조퇴시 얼마나 차이나는지 저장
    @Column(nullable = true)
    private Integer minutes;

}
