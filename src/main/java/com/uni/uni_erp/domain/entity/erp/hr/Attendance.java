package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.util.date.DateFormatter;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;
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

    @Column(nullable = true)
    private Integer wage; // 시급

    @Column(name = "missed_time", nullable = true)
    private Integer missedTime; // 지각 조퇴 등으로 지켜지지 못한 시간

    // 출석 상태를 관리하는 enum
    @RequiredArgsConstructor
    @Getter
    public enum Status {
        NOT_EXECUTED("이행되지 않음"),
        WORKING("근무중"),
        ATTENDED("출석"),
        LATE("지각"),
        LEFT_EARLY("조퇴"),
        LATE_AND_LEFT_EARLY("지각&&조퇴"),
        ABSENT("결근"),
        UNPLANNED_WORK("계획에 없는 근무");

        private final String description;
    }

    /**
     * 1. status 저장
     * 2. 정산용 근무 시간 저장
     * 3. 시급 저장
     * 4. 지각 및 조퇴시 해당 시간 저장
     * 5. 근무 일정도 완료로 변경
     */
    public void setValuesAtLeave() {
        // 예정에 있던 근무일 경우
        LocalDateTime plannedStartTime = null;
        LocalDateTime plannedEndTime = null;
        LocalDateTime attendanceTime = DateFormatter.toLocalDateTime(startTime);
        LocalDateTime leaveTime = DateFormatter.toLocalDateTime(endTime);
        if (schedule != null) {
            plannedStartTime = DateFormatter.toLocalDateTime(schedule.getStartTime());
            plannedEndTime = DateFormatter.toLocalDateTime(schedule.getEndTime());
            long minutesDifference = Duration.between(plannedStartTime, plannedEndTime).toMinutes();
            // 4시간 마다 휴게 30분이 주어지기 때문에 240분의 몫 * 30 만큼 차감
            this.workTime = (int) (minutesDifference - (minutesDifference / 240) * 30);
            schedule.setStatus(Schedule.Status.COMPLETED);
        }
        if (schedule == null) {
            this.workTime = (int) Duration.between(attendanceTime, leaveTime).toMinutes();
            this.status = Status.UNPLANNED_WORK;
        } else if (plannedStartTime.isBefore(attendanceTime) && plannedEndTime.isAfter(leaveTime)) {
            this.status = Status.LATE_AND_LEFT_EARLY;
            // 지각 시간 (분 단위 계산)
            int lateMinutes = (int) Duration.between(plannedStartTime, attendanceTime).toMinutes();

            // 조퇴 시간 (분 단위 계산)
            int earlyLeaveMinutes = (int) Duration.between(leaveTime, plannedEndTime).toMinutes();
            this.missedTime = earlyLeaveMinutes + lateMinutes;
        } else if (plannedStartTime.isBefore(attendanceTime)) {
            this.status = Status.LATE;
            // 지각 시간 (분 단위 계산)
            int lateMinutes = (int) Duration.between(plannedStartTime, attendanceTime).toMinutes();
            this.missedTime = lateMinutes;
        } else if (plannedEndTime.isAfter(leaveTime)) {
            this.status = Status.LEFT_EARLY;
            // 조퇴 시간 (분 단위 계산)
            int earlyLeaveMinutes = (int) Duration.between(leaveTime, plannedEndTime).toMinutes();
            this.missedTime = earlyLeaveMinutes;
        } else {
            this.status = Status.ATTENDED;
        }
        // TODO 시급 변경 해야함
        this.wage = 9860;
    }
}
