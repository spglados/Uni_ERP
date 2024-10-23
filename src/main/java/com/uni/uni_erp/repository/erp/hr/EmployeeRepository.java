package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.dto.erp.hr.EmpDocumentDTO;
import com.uni.uni_erp.dto.erp.hr.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // 상점에서 관리하는 모든 직원(Employee) 목록을 가져오는 쿼리 메서드
    List<Employee> findByStoreId(Integer storeId); // Employee 반환

    // 가게별 직원 목록
    @Query("SELECT new com.uni.uni_erp.dto.erp.hr.EmployeeDTO(e) FROM Employee e WHERE e.store.id = :storeId")
    List<EmployeeDTO> findEmployeesByStoreId(@Param("storeId") Integer storeId);

    // 직원 상세
    @Query("SELECT e FROM Employee e " +
            "JOIN FETCH e.bank " +
            "JOIN FETCH e.empPosition " +
            "JOIN FETCH e.empDocument " +
            "WHERE e.id = :employeeId")
    Optional<Employee> findEmployeeWithAllDetails(@Param("employeeId") Integer employeeId);

    // 직원과 은행, 상점 정보를 함께 가져오는 쿼리
    @Query("SELECT e FROM Employee e JOIN FETCH e.bank JOIN FETCH e.store")
    List<Employee> findAllWithBankAndStore(); // Employee 반환

    // 가게별로 가장 큰 사원 번호 조회
    @Query("SELECT COALESCE(MAX(e.storeEmployeeNumber), 0) FROM Employee e WHERE e.store.id = :storeId")
    Integer findMaxStoreEmployeeNumberByStoreId(@Param("storeId") Integer storeId);

    Optional<Employee> findByUniqueEmployeeNumber(String uniqueEmployeeNumber); // 유니크 직원 번호로 조회

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.empDocument WHERE e.id = :id")
    Employee findByIdWithDocuments(@Param("id") Integer id);

    // 특정 직원의 문서 정보를 가져오는 메서드
    @Query("SELECT d FROM EmpDocument d WHERE d.employee.id = :employeeId")
    EmpDocument findByEmployeeId(@Param("employeeId") Integer employeeId); // EmpDocument 반환

    // 특정 상점에서 직책에 해당하는 모든 직원 조회
    @Query("SELECT e FROM Employee e WHERE e.empPosition.store.id = :storeId AND e.empPosition.id = :positionId")
    List<Employee> findByStoreIdAndPositionId(@Param("storeId") Integer storeId, @Param("positionId") Integer positionId); // Employee 반환

    // 이메일로 직원 존재 여부 체크
    boolean existsByEmail(String email);

    // 전화번호로 직원 존재 여부 체크
    boolean existsByPhone(String phone);

    // 이메일 찾기
    Optional<Employee> findByEmail(String email);
    // 전화번호 찾기
    Optional<Employee> findByPhone(String phone);


}


