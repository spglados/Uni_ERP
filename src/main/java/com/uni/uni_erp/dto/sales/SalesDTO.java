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


    @Data
    @Builder
    @NoArgsConstructor
    public static class SalesQuantityDTO {

        private int orderNum;
        private String itemCode;
        private String itemName;
        private int quantity;
        private SalesDetail.SaleStatus status;
        private LocalDateTime salesDate;

        public SalesQuantityDTO(int orderNum, String itemCode, String itemName, int quantity, SalesDetail.SaleStatus status, LocalDateTime salesDate) {
            this.orderNum = orderNum;
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.quantity = quantity;
            this.status = status;
            this.salesDate = salesDate;
        }

    }
}
