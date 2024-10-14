package com.uni.uni_erp.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDTO {

    private int id;

    private String name;

    private double amount;

    private String unit;

    private int productId;

}
