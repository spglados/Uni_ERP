package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.domain.entity.erp.product.MaterialStatus;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT ms " +
            "FROM MaterialStatus ms " +
            "JOIN ms.material m " +
            "JOIN m.store s " +
            "WHERE m.alarmUnit = m.unit " +
            "AND s.id = :storeId " +
            "AND (ms.theoreticalAmount < m.alarmCycle OR ms.actualAmount < m.alarmCycle) " +
            "AND ms.statusDate = :today ")
    List<MaterialStatus> findAlarmCycleMaterialDTOByStoreId(@Param("storeId") Integer storeId, LocalDate today);

    @Query("SELECT ms FROM MaterialStatus ms WHERE ms.material.id IN :materialIds")
    List<MaterialStatus> findByMaterialIds(@Param("materialIds") List<Integer> materialIds);

    @Query("SELECT ms FROM MaterialStatus ms WHERE MONTH(ms.statusDate) = :currentMonth")
    List<MaterialStatus> findByCurrentMonth(int currentMonth);

    @Query("SELECT ms FROM MaterialStatus ms WHERE ms.material.store.id = :storeId AND ms.statusDate = :now")
    List<MaterialStatus> findAllByStoreIdAndToday(Integer storeId, LocalDate now);
}
