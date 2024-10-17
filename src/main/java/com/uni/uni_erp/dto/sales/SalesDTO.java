package com.uni.uni_erp.dto.sales;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class SalesDTO {
    private Integer id;
    private Integer orderNum;
    private Integer totalPrice;
    private LocalDateTime salesDate;
}
