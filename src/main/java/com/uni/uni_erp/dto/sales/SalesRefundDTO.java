package com.uni.uni_erp.dto.sales;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesRefundDTO {

    private Integer id;
    private Long itemCode;
    private String itemName;
    private Integer quantity;
    private Integer unitPrice;

    private enum Status {REFUND, RETURN}

}
