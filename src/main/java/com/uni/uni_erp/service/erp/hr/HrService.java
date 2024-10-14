package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrService {

    private final EmployeeRepository hrRepository;
    private final StoreRepository storeRepository;

    public List<Employee> getEmployeesByStoreId(Integer storeId) {
        List<Employee> employees = hrRepository.findByStoreId(storeId);
        System.out.println("Employees found for store ID " + storeId + ": " + employees.size());
        if (employees.isEmpty()) {
            System.out.println("No employees found for store ID " + storeId);
        } // 직원 수 로그 추가
        return employees;
    }

    // 신규 직원 등록
    public Employee registerEmployee(EmployeeDTO employeeDTO, Integer storeId) {
        Employee employee = null;
        // storeId 로 스토어 조회
        try {
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new Exception500("서버 에러"));
            employee = Employee.builder()
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
        } catch (Exception e) {
            // TODO 예외처리
        }


        return hrRepository.save(employee); // 저장
    }

}
