package com.uni.uni_erp.repository.pos;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PosRepository extends JpaRepository<Product, Integer> {
     List<Product> findByStoreId(Integer storeId);


}
