package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.util.date.DateFormatter;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "schedule_id", nullable = true)
    private Schedule schedule;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.NOT_EXECUTED;

    @Column(name = "work_time", nullable = true)
    private Integer workTime; // 정산용 근무 시간을 분단위로 저장

    private Integer wage; // 시급

    // 출석 상태를 관리하는 enum
    @RequiredArgsConstructor
    @Getter
    public enum Status {
        NOT_EXECUTED("이행되지 않음"),
        WORKING("근무중"),
        ATTENDED("출석"),
        LATE("지각"),
        LEFT_EARLY("조퇴"),
        ABSENT("결근"),
        UNPLANNED_WORK("계획에 없는 근무");

        private final String description;
    }

    public void setValuesAtLeave() {
        // TODO 퇴근 로직 일단 미룸
        LocalDateTime plannedStart = DateFormatter.toLocalDateTime(schedule.getStartTime());
        LocalDateTime plannedEnd = DateFormatter.toLocalDateTime(schedule.getEndTime());
        LocalDateTime workedStart = DateFormatter.toLocalDateTime(this.startTime);
        LocalDateTime workedEnd = DateFormatter.toLocalDateTime(this.endTime);
    }
}
