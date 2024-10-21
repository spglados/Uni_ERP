package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ScheduleDTO {

    @Data
    public static class CreateDTO {
        private Integer empId;
        private String startTime;
        private String endTime;
        private String type;

        public Schedule toEntity(Store store, Employee emp) {
            return Schedule.builder()
                    .store(store)
                    .employee(emp)
                    .startTime(Timestamp.valueOf(startTime.replace("T", " ")))
                    .endTime(Timestamp.valueOf(endTime.replace("T", " ")))
                    .build();
        }
    }

    /**
     * Fullcalendar API 의 이벤트 양식을 위한 DTO
     */
    @Data
    @Builder
    public static class ResponseDTO {

        private String id; // Schedule.id
        private String title; // Employee.name
        private String start; // Schedule.startTime
        private String end; // Schedule.endTime
        private CustomProperty extendedProps; // 커스텀 속성
        private boolean allDay;

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
