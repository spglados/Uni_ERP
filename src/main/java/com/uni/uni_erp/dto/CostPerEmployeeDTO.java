package com.uni.uni_erp.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CostPerEmployeeDTO {

    private Integer id;
    private String name;
    private Integer costPer;
    private Integer totalOrders;

}
