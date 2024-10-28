package com.uni.uni_erp.repository.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.ProductDisposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDisposalRepository extends JpaRepository<ProductDisposal, Integer> {

    @Query("SELECT pd FROM ProductDisposal pd WHERE pd.product.store.id = :storeId")
    List<ProductDisposal> findByStoreId(Integer storeId);

}
