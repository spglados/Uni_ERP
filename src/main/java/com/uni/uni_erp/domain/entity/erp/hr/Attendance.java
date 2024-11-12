package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.store.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "hr_attendance_tb")
public class
Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    @Column(name = "break_time", nullable = true)
    private Integer breakTime; // 휴식 시간

    @Column(nullable = true)
    private Integer wage; // 시급

    @Column(name = "missed_time", nullable = true)
    private Integer missedTime; // 지각 조퇴 등으로 지켜지지 못한 시간

    @Column(name = "overed_time", nullable = true)
    private Integer overedTime; // 기존 일정과 비교하여 초과된 시간

    // 출석 상태를 관리하는 enum
    @RequiredArgsConstructor
    @Getter
    public enum Status {
        NOT_EXECUTED("출근전"),
        WORKING("근무중"),
        ATTENDED("정상"),
        LATE("지각"),
        LEFT_EARLY("조퇴"),
        LATE_AND_LEFT_EARLY("지각&조퇴"),
        SICK_ABSENT("병가"),
        UNAUTHORIZED_ABSENT("무단결근"),
        PERSONAL_ABSENT("개인사정"),
        UNPLANNED_WORK("계획 외");

        private final String description;
    }

}
