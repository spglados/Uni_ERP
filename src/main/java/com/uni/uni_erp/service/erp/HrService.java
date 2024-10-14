package com.uni.uni_erp.service.erp;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.repository.erp.HrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrService {

    private final HrRepository hrRepository;

    public List<Employee> getEmployeesByUserId(Integer storeId) {
        List<Employee> employees = hrRepository.findByStoreId(userId);
        System.out.println("Employees found for user ID " + userId + ": " + employees.size()); // 직원 수 로그 추가
        return employees;
    }

    // 신규 직원 등록
    public Employee registerEmployee(EmployeeDTO employeeDTO, Integer storeId) {
        // TODO 임시 스토어
        Store store = new Store();
        store.setId(storeId);
        Employee employee = Employee.builder()
                .name(employeeDTO.getName())
                .birthday(employeeDTO.getBirthday())
                .gender(employeeDTO.getGender())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .accountNumber(employeeDTO.getAccountNumber())
                .position(employeeDTO.getPosition())
                .store(store)
                .employmentStatus(Employee.EmploymentStatus.ACTIVE) // 기본 상태
                .build();

        return hrRepository.save(employee); // 저장
    }

}
