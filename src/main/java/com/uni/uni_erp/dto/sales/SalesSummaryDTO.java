package com.uni.uni_erp.dto.sales;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesSummaryDTO {
    private String itemName;
    private Integer totalQuantity;
    private Integer unitPrice; // Original unit price

}