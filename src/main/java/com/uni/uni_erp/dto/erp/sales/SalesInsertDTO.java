package com.uni.uni_erp.dto.erp.sales;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesInsertDTO {
    private Integer orderNum;
    private Integer totalPrice;
    private LocalDateTime salesDate;
    private Integer storeId;
}
