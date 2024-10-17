package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.MaterialOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialOrderRepository extends JpaRepository<MaterialOrder, Integer> {

    @Query("SELECT o FROM MaterialOrder o WHERE o.material.id IN :materialId")
    public List<MaterialOrder> findByMaterialId(List<Integer> materialId);

}
