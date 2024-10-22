package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ScheduleDTO {

    @Data
    public static class CreateDTO {
        private Integer empId;
        private String startTime;
        private String endTime;

        public Schedule toEntity(Store store, Employee emp) {
            return Schedule.builder()
                    .store(store)
                    .employee(emp)
                    .startTime(Timestamp.valueOf(startTime.replace("T", " ")))
                    .endTime(Timestamp.valueOf(endTime.replace("T", " ")))
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer id;
        private String startTime;
        private String endTime;

    }

    /**
     * Fullcalendar API 의 이벤트 양식을 위한 DTO
     */
    @Getter
    @Setter
    public static class ResponseDTO {

        private String id; // Schedule.id
        private String title; // Employee.name
        private String start; // Schedule.startTime
        private String end; // Schedule.endTime
        private CustomProperty extendedProps; // 커스텀 속성
        private boolean allDay;

        public ResponseDTO(Schedule schedule) {
            this.id = String.valueOf(schedule.getId());
            this.title = schedule.getEmployee().getName();
            this.start = DateFormatter.toIsoFormat(schedule.getStartTime());
            this.end = DateFormatter.toIsoFormat(schedule.getEndTime());
            this.extendedProps = CustomProperty.builder()
                    .empId(schedule.getEmployee().getId())
                    .status(EnumCommonUtil.getStringFromEnum(schedule.getStatus()))
                    .minutes(schedule.getMinutes() == null ? 0 : schedule.getMinutes())
                    .build();
            this.allDay = false;
        }

    }

    /**
     * 커스텀 속성
     */
    @Data
    @Builder
    public static class CustomProperty {
        private Integer empId; // Employee.id
        private String status; // Schedule.Status
        private Integer minutes;
    }


}
