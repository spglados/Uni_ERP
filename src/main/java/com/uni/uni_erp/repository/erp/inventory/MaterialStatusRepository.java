package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.domain.entity.erp.product.MaterialStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MaterialStatusRepository extends JpaRepository<MaterialStatus, Integer> {

    @Query("SELECT ms FROM MaterialStatus ms JOIN FETCH ms.material m JOIN FETCH m.store s WHERE s.id = :storeId AND ms.statusDate = (SELECT MAX(ms2.statusDate) FROM MaterialStatus ms2 WHERE ms2.material.id = ms.material.id) ORDER BY ms.statusDate DESC")
    List<MaterialStatus> findByStoreId(Integer storeId);

    @Query("SELECT ms FROM MaterialStatus ms JOIN FETCH ms.material m JOIN FETCH m.store s WHERE s.id = :storeId AND ms.statusDate = :today")
    List<MaterialStatus> findByStoreIdAndStatusDate(Integer storeId, LocalDate today);

    @Query("SELECT ms FROM MaterialStatus ms WHERE ms.material.id = :materialId AND ms.statusDate = (SELECT MAX(ms2.statusDate) FROM MaterialStatus ms2 WHERE ms2.material.id = :materialId)")
    MaterialStatus findByMaterialId(Integer materialId);

    @Query("SELECT ms FROM MaterialStatus ms WHERE ms.material IN :materials AND ms.statusDate = :today")
    List<MaterialStatus> findByMaterial(List<Material> materials, LocalDate today);
}
