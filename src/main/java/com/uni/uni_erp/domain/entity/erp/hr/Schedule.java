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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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

    @OneToOne(mappedBy = "schedule", fetch = FetchType.LAZY)
    private Attendance attendance;

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        NOT_EXECUTED("이행되지 않음"),
        COMPLETED("완료");

        private final String description;
    }

}
