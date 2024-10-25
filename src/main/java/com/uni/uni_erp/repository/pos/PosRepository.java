package com.uni.uni_erp.repository.pos;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PosRepository extends JpaRepository<Product, Integer> {

     // 그 가게의 전체 품목목록
     Page<Product> findByStoreId(Integer storeId, Pageable pageable);

     // 그 가게의 품목중 카테고리 선택
     //Page<Product> findByStoreIdAndCategory(Integer storeId, String category, Pageable pageable);
     @Query("SELECT p FROM Product p WHERE p.store.id = :storeId AND (:category IS NULL OR :category = '' OR p.category = :category)")
     Page<Product> findByStoreIdAndCategory(@Param("storeId") Integer storeId, @Param("category") String category, Pageable pageable);

}
