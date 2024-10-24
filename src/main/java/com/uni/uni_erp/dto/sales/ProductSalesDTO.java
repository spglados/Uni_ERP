package com.uni.uni_erp.dto.sales;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ProductSalesDTO {

    // Product name
    private String productName;

    // Sales figures for the product
    private Integer monthlySales;
    private Integer lastMonthSales;
    private Double monthlyGrowthRate;

    private Integer yearlySales;
    private Integer lastYearSales;
    private Double yearlyGrowthRate;

    // Profit for the product
    private Integer profit;

}