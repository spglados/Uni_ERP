package com.uni.uni_erp.repository.erp.hr;


import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.dto.erp.hr.EmpPositionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpPositionRepository extends JpaRepository<EmpPosition, Integer> {

        // storeId별 직책 조회
        @Query("SELECT p FROM EmpPosition p WHERE p.store.id = :storeId")
        List<EmpPosition> findByStoreId(@Param("storeId") Integer storeId);
}
