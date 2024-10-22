package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.MaterialOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialOrderRepository extends JpaRepository<MaterialOrder, Integer> {

    @Query("SELECT o FROM MaterialOrder o WHERE o.material.id IN :materialId")
    List<MaterialOrder> findByMaterialId(List<Integer> materialId);

    @Query("SELECT mo FROM MaterialOrder mo JOIN FETCH mo.material m JOIN FETCH m.store s WHERE s.id = :storeId ORDER BY mo.enterDate DESC")
    List<MaterialOrder> findByStoreId(Integer storeId);
}
