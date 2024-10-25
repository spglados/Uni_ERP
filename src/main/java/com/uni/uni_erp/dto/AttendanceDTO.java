package com.uni.uni_erp.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AttendanceDTO {

    private Integer employeeId;
    private String employeeName;

}
