package com.uni.uni_erp.dto.sales;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesSummaryDTO {
    private String itemName;
    private int totalQuantity;
    private double unitPrice; // Original unit price

    // Getters and Setters (if needed)
}
