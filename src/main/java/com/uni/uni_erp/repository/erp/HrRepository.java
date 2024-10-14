package com.uni.uni_erp.repository.erp;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HrRepository  extends JpaRepository<Employee, Integer> {

    // 고용주가 관리하는 모든 직원(Employee) 목록을 가져오는 쿼리 메서드
    List<Employee> findByOwnerId(Integer ownerId);

    // 고용주의 특정 직원(Employee)을 조회하는 메서드
    Employee findByOwnerIdAndId(Integer ownerId, Integer employeeId);

}
