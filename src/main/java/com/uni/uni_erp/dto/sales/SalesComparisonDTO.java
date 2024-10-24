package com.uni.uni_erp.dto.sales;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesComparisonDTO {

    // Time-related field (could be the hour, for example)
    private Integer hour;

    // Last day data
    private Integer lastDayTargetProfit;
    private Integer lastDayTotalSales;
    private Integer lastDaySalesCount;

    // Today data
    private Integer todayTargetProfit;
    private Integer todayTotalSales;
    private Integer todaySalesCount;

    // Comparison fields
    private Integer salesComparedToLastDay; // today - last day total sales difference
    private Double percentageComparedToLastDay; // percentage difference compared to the last day

    private Integer salesComparedToLastYear; // today - last year total sales difference
    private Double percentageComparedToLastYear; // percentage difference compared to the same day last year
}
