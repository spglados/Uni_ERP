package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmpPositionDTO {
    private Integer id;
    private String name;
    private String scheduleColor;

    // EmpPosition을 DTO로 변환하는 생성자
    public EmpPositionDTO(EmpPosition empPosition) {
        this.id = empPosition.getId();
        this.name = empPosition.getName();
        this.scheduleColor = empPosition.getScheduleColor();
    }
}
