package com.uni.uni_erp.dto.erp.sales;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesbyCategoryDTO {

    private String category;
    private Long totalUnitPrice;
}
