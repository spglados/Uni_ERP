package com.uni.uni_erp.dto.sales;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.domain.entity.SalesDetail;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SalesDTO {
    private Integer id;
    private Integer orderNum;
    private Integer totalPrice;
    private LocalDateTime salesDate;

}
