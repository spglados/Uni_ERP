package com.uni.uni_erp.repository.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;

public interface IngredientRepository extends JpaAttributeConverter<Ingredient, Integer> {
}
