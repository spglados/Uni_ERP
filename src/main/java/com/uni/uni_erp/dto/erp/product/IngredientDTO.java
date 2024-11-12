package com.uni.uni_erp.dto.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.util.str.UnitCategory;
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

    private String subUnit;

    private int productId;

    public IngredientDTO(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.amount = ingredient.getAmount();
        this.unit = String.valueOf(ingredient.getUnit());
        this.subUnit = String.valueOf(ingredient.getMaterial().getSubUnit());
        this.productId = ingredient.getProduct().getId();
    }

    public Ingredient toIngredient(Product product) {
        return Ingredient.builder()
                .name(this.name)
                .amount(this.amount)
                .unit(UnitCategory.valueOf(this.unit.toUpperCase()))
                .product(product)
                .build();
    }

}
