package com.uni.uni_erp.repository.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

}