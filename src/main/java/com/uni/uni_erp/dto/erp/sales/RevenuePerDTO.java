package com.uni.uni_erp.dto.erp.sales;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class RevenuePerDTO {
    private String name;
    private Integer weeklyCostPer;
    private Integer weeklyTotalOrders;
    private Integer monthlyCostPer;
    private Integer monthlyTotalOrders;
    private Integer yearlyCostPer;
    private Integer yearlyTotalOrders;
}
