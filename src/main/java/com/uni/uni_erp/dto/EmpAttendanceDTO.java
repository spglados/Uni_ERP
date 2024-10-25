package com.uni.uni_erp.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class EmpAttendanceDTO {

    private Integer employeeId;
    private String employeeName;

}
