package com.uni.uni_erp.dto.sales;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesDetailDTO {

    private Integer id;
    private Long itemCode;
    private String itemName;
    private Integer quantity;
    private Integer unitPrice;

}
