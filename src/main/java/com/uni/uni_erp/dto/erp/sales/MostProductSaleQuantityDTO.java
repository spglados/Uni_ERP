package com.uni.uni_erp.dto.erp.sales;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MostProductSaleQuantityDTO {

    private long productCode;
    private String productName;
    private long quantity;

    public MostProductSaleQuantityDTO(long productCode, String productName, long quantity) {
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
    }

}
