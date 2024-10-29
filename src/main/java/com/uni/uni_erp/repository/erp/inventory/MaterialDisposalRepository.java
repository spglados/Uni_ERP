package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.MaterialDisposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialDisposalRepository extends JpaRepository<MaterialDisposal, Integer> {

    @Query("SELECT md FROM MaterialDisposal md WHERE md.material.store.id = :storeId")
    List<MaterialDisposal> findByStoreId(Integer storeId);

}
