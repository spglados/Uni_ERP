package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // 상점에서 관리하는 모든 직원(Employee) 목록을 가져오는 쿼리 메서드
    List<Employee> findByStoreId(Integer storeId);

    // 이름으로 직원 리스트 조회 (부분 일치)
    List<Employee> findByName(String name);

    // 직원과 은행, 상점 정보를 함께 가져오는 쿼리
    @Query("SELECT e FROM Employee e JOIN FETCH e.bank JOIN FETCH e.store")
    List<Employee> findAllWithBankAndStore();

    // 가게별로 가장 큰 사원 번호 조회
    @Query("SELECT COALESCE(MAX(e.storeEmployeeNumber), 0) FROM Employee e WHERE e.store.id = :storeId")
    Integer findMaxStoreEmployeeNumberByStoreId(@Param("storeId") Integer storeId);

    Optional<Employee> findByUniqueEmployeeNumber(String uniqueEmployeeNumber); // 유니크 직원 번호로 조회 // 직원 ID로 조회

    // 특정 직원의 문서 정보를 가져오는 메서드
    @Query("SELECT d FROM EmpDocument d WHERE d.employee.id = :employeeId")
    List<EmpDocument> findByEmployeeId(@Param("employeeId") Integer employeeId);

}
