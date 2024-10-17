package com.uni.uni_erp.repository.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientsRepository extends JpaRepository<Ingredient, Integer> {

    @Query("SELECT i FROM Ingredient i JOIN FETCH i.product WHERE i.product.id = :productId")
    public List<Ingredient> findByProductId(int productId);

}
