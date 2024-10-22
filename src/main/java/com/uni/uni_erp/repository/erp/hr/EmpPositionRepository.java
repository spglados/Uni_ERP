package com.uni.uni_erp.repository.erp.hr;


import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.dto.erp.hr.EmpPositionDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpPositionRepository extends JpaRepository<EmpPosition, Integer> {
        List<EmpPosition> findAll(); // EmpPositionDTO로 반환
}
