package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // 상점에서 관리하는 모든 직원(Employee) 목록을 가져오는 쿼리 메서드
    List<Employee> findByStoreId(Integer storeId);

}
