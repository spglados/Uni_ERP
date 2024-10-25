package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpDocumentRepository extends JpaRepository<EmpDocument, Integer> {

    // 직원 문서 조회
    Optional<EmpDocument> findByEmployeeId(Integer employeeId);

    // 직원 ID로 문서 존재 여부 확인
    Boolean existsByEmployeeId(Integer employeeId);

    @Query("SELECT e FROM Employee e JOIN FETCH e.empDocument ed WHERE e.store.id = :storeId")
    List<Employee> findByStoreIdWithDocuments(@Param("storeId") Integer storeId);
}
