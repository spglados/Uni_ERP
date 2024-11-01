package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AttendanceDTO {

    /**
     * 직원 출퇴근 요청 페이지 용 DTO
     */
    @Data
    @NoArgsConstructor
    public static class ResponseDTO {
        private Integer id;
        private String name;
        private String start; // 근무 일정상 시작
        private String end; // 근무 일정상 종료
        private String attendanceTime; // 출근 시간
        private String leaveTime; // 퇴근 시간
        private String status; // 상태

        public ResponseDTO(Attendance attendance) {
            this.id = attendance.getId();
            this.name = attendance.getEmployee().getName();
            if (attendance.getSchedule() != null) {
                this.start = DateFormatter.toDateAndTimeExcludeYear(attendance.getSchedule().getStartTime());
                this.end = DateFormatter.toTimeHourAndMinute(attendance.getSchedule().getEndTime());
            }
            if (attendance.getStartTime() != null) {
                this.attendanceTime = DateFormatter.toTimeHourAndMinute(attendance.getStartTime());
            }
            if (attendance.getEndTime() != null) {
                this.leaveTime = DateFormatter.toTimeHourAndMinute(attendance.getEndTime());
            }
            this.status = EnumCommonUtil.getStringFromEnum(attendance.getStatus());
        }

    }

    /**
     * 출퇴근 등록 용 DTO
     */
    @Data
    @NoArgsConstructor
    public static class RequestDTO {
        private Integer id;
        private String type;
        private String password;
    }

    /**
     * 근태 관리 페이지 용 DTO
     */
    @Data
    @NoArgsConstructor
    public static class GridDTO {
        private Long empNo;
        private String name;
        private LocalDate date;
        private LocalDateTime attendanceTime;
        private LocalDateTime leaveTime;
        private Integer breakTime; // 분단위
        private Integer workTime; // 분단위
        private Integer overedTime; // 분단위
        private Integer missedTime; // 분단위
        private Integer wage; // 시급
        private String status; // 상태
        private LocalDateTime startTime; // 근무 일정 상 출근 시간
        private LocalDateTime endTime; // 근무 일정 상 퇴근 시간

        public GridDTO(Attendance attendance) {
            this.empNo = attendance.getEmployee().getUniqueEmployeeNumber();
            this.name = attendance.getEmployee().getName();
            this.date = DateFormatter.toLocalDate(attendance.getStartTime());
            this.attendanceTime = DateFormatter.toLocalDateTime(attendance.getStartTime());
            this.leaveTime = DateFormatter.toLocalDateTime(attendance.getEndTime());
            this.breakTime = attendance.getBreakTime();
            this.workTime = attendance.getWorkTime();
            this.overedTime = attendance.getOveredTime();
            this.missedTime = attendance.getMissedTime();
            this.wage = attendance.getWage();
            this.status = attendance.getStatus().getDescription();
            if (attendance.getSchedule() != null) {
                this.startTime = DateFormatter.toLocalDateTime(attendance.getSchedule().getStartTime());
                this.endTime = DateFormatter.toLocalDateTime(attendance.getSchedule().getEndTime());
            }
        }
    }

    /**
     * erp 메인 용 오늘 출근 예정자 리스트 DTO
     */
    @Data
    @NoArgsConstructor
    public static class TodayAttendanceDTO {
        private Long empNo;       // 사번
        private String name;      // 이름
        private String startTime; // 출근 예정 시간 - 예정 외 근무는 "예정 외"
        private String status;    // 출근전 --> 근무중 --> 퇴근

        public TodayAttendanceDTO(Attendance attendance) {
            this.empNo = attendance.getEmployee().getUniqueEmployeeNumber();
            this.name = attendance.getEmployee().getName();
            if (attendance.getSchedule() != null) {
                this.startTime = DateFormatter.toTimeHourAndMinute(attendance.getSchedule().getStartTime());
            } else {
                this.startTime = "예정 외";
            }
            this.status = attendance.getStatus().getDescription();
            if (status.equals("정상") || status.equals("예정 외")) status = "퇴근";
        }
    }

    /**
     * erp 메인 용 이번 달 근태 불량 수치 리스트 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyAttendanceDTO {
        private Long empNo;                 // 사번
        private String name;                // 이름
        private Integer late;               // 지각
        private Integer earlyLeave;         // 조퇴
        private Integer unauthorizedAbsent; // 무단 결근
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErpMainDTO {
        private List<TodayAttendanceDTO> todayAttendanceList;
        private List<MonthlyAttendanceDTO> monthlyAttendanceList;
    }

}
