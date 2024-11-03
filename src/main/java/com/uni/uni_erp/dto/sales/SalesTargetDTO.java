package com.uni.uni_erp.dto.sales;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesTargetDTO {

    private Integer hour;
    private Integer targetProfit;
    private Integer sales;
    private Integer salesCount;

}
