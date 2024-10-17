package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpDocumentRepository extends JpaRepository<EmpDocument, Integer> {

    // 직원 문서 조회
    Optional<EmpDocument> findByEmployeeId(Integer employeeId);

    // 직원 ID로 문서 존재 여부 확인
    Boolean existsByEmployeeId(Integer employeeId);

}
