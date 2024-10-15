package com.uni.uni_erp.repository.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.store.id = :storeId")
    public List<String> findProductNameByStoreId(Integer storeId);

    public List<Product> findProductByStoreId(Integer storeId);

}
