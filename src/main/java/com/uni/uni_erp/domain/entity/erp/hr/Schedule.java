package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.erp.hr.ScheduleDTO;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import com.uni.uni_erp.util.date.DateFormatter;
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

    @Column(name = "start_time", updatable = false)
    private Timestamp startTime;

    @Column(name = "end_time", updatable = false)
    private Timestamp endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    public enum ScheduleType {
        PLAN, // 계획
        EXECUTE // 실행
    }

    public ScheduleDTO.ResponseDTO toResponseDTO() {
        return ScheduleDTO.ResponseDTO.builder()
                .id(String.valueOf(id))
                .title(employee.getName())
                .startTime(DateFormatter.toIsoFormat(startTime))
                .endTime(DateFormatter.toIsoFormat(endTime))
                .extendedProps(ScheduleDTO.CustomProperty.builder()
                        .empId(employee.getId())
                        .type(EnumCommonUtil.getStringFromEnum(scheduleType))
                        .build())
                .build();
    }
}
