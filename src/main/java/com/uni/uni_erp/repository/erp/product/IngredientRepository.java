package com.uni.uni_erp.repository.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    public List<Ingredient> findByProductId(int productId);

}
