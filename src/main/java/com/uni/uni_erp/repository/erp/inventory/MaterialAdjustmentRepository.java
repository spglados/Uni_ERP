package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.MaterialAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MaterialAdjustmentRepository extends JpaRepository<MaterialAdjustment, Integer> {

    @Query("SELECT ma FROM MaterialAdjustment ma WHERE ma.material.id = :materialId AND ma.statusDate = (SELECT MAX(ma2.statusDate) FROM MaterialAdjustment ma2 WHERE ma2.material.id = :materialId)")
    public MaterialAdjustment findByMaterialId(Integer materialId);

}
