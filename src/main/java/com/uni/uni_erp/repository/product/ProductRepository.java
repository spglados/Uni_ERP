package com.uni.uni_erp.repository.product;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
