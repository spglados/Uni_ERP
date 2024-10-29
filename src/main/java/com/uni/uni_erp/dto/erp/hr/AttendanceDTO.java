package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        Long empNo;
        String name;
        LocalDate date;
        LocalDateTime attendanceTime;
        LocalDateTime leaveTime;
        Integer breakTime; // 분단위
        Integer workTime; // 분단위
        Integer overedTime; // 분단위
        Integer missedTime; // 분단위
        Integer wage; // 시급
        String status; // 상태
        LocalDateTime startTime; // 근무 일정 상 출근 시간
        LocalDateTime endTime; // 근무 일정 상 퇴근 시간

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

}
