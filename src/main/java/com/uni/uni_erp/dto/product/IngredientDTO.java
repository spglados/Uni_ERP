
package com.uni.uni_erp.dto.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.util.Str.UnitCategory;
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

    public Ingredient toIngredient(Product product) {
        return Ingredient.builder()
                .name(this.name)
                .amount(this.amount)
                .unit(UnitCategory.valueOf(this.unit.toUpperCase()))
                .product(product)
                .build();
    }

}