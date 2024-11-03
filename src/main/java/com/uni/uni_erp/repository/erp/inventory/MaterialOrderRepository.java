package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.MaterialOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaterialOrderRepository extends JpaRepository<MaterialOrder, Integer> {

    @Query("SELECT mo FROM MaterialOrder mo JOIN FETCH mo.material m JOIN FETCH m.store s WHERE s.id = :storeId ORDER BY mo.enterDate DESC")
    List<MaterialOrder> findByStoreId(Integer storeId);

    @Query("SELECT o FROM MaterialOrder o WHERE o.material.id IN :materialIdList AND o.isUse = true ORDER BY o.expirationDate ASC")
    List<MaterialOrder> findByMaterialIdAndUseStatus(List<Integer> materialIdList);

    @Query("SELECT o FROM MaterialOrder o WHERE o.material.id IN :materialIdList AND o.isUse = true AND o.material.store.id = :storeId ORDER BY o.expirationDate ASC")
    List<MaterialOrder> findByMaterialIdAndUseStatusAndStoreId(List<Integer> materialIdList, Integer storeId);

    @Query("SELECT mo FROM MaterialOrder mo " +
            "WHERE FUNCTION('YEAR', mo.enterDate) = :year " +
            "AND FUNCTION('MONTH', mo.enterDate) = :month " +
            "AND mo.material.store.id = :storeId")
    List<MaterialOrder> findByEnterDateInCurrentMonthAndStoreId(@Param("year") int year,
                                                                @Param("month") int month,
                                                                @Param("storeId") Integer storeId);

    @Query("SELECT mo FROM MaterialOrder mo WHERE mo.material.id IN :materialIds")
    List<MaterialOrder> findByMaterialIds(@Param("materialIds") List<Integer> materialIds);

}
