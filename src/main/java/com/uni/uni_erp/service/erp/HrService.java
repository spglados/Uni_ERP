package com.uni.uni_erp.service.erp;

import com.uni.uni_erp.domain.entity.Employee;
import com.uni.uni_erp.repository.erp.HrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrService {

    private final HrRepository hrRepository;

    public List<Employee> getEmployeesByUserId(Integer userId) {
        List<Employee> employees = hrRepository.findByOwnerId(userId);
        System.out.println("Employees found for user ID " + userId + ": " + employees.size()); // 직원 수 로그 추가
        return employees;
    }


}
