package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.*;

public class AttendanceDTO {

    @Data
    @NoArgsConstructor
    public static class ResponseDTO {
        private String name;
        private String start; // 근무 일정상 시작
        private String end; // 근무 일정상 종료
        private String attendanceTime; // 출근 시간
        private String leaveTime; // 퇴근 시간
        private String status; // 상태

        public ResponseDTO(Attendance attendance) {
            this.name = attendance.getEmployee().getName();
            if (attendance.getSchedule() != null) {
                this.start = DateFormatter.toTimeHourAndMinute(attendance.getSchedule().getStartTime());
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

}
