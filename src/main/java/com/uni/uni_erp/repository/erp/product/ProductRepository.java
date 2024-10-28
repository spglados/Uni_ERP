package com.uni.uni_erp.repository.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.productCode IN :productCodes AND p.store.id = :storeId")
    public List<Product> findAllByStoreIdForProductCode(Integer storeId, List<Long> productCodes);

    public List<Product> findProductByStoreId(Integer storeId);

    @Query("SELECT p FROM Product p WHERE p.productCode = :productCode")
    Optional<Product> findByProductCode(Long productCode);
}
