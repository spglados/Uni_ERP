package com.uni.uni_erp.dto.product;

import lombok.*;

import java.text.DecimalFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private int id;

    // UserId + StoreId + pk 형식 유지
    private int productCode;

    private String name;

    private String category;

    private int price;

    private int storeId;

    public String formatToPrice() {
        // DecimalFormat을 사용하여 3자리마다 콤마를 넣고, 앞에 \를 붙임
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(this.price);
    }

}
