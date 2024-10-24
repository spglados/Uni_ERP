package com.uni.uni_erp.dto.sales;

import com.uni.uni_erp.domain.entity.SalesDetail;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class SalesQuantityDTO {

    private Long itemCode;
    private int quantity;
    private LocalDateTime salesDate;

    public SalesQuantityDTO(Long itemCode, int quantity, LocalDateTime salesDate) {
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.salesDate = salesDate;
    }
}
