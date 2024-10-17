package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.Bank;
import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.repository.bank.BankReposigory;
import com.uni.uni_erp.repository.erp.hr.EmpDocumentRepository;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HrService {

    private final EmployeeRepository hrRepository;
    private final StoreRepository storeRepository;
    private final BankReposigory  bankRepository;
    private final EmpDocumentRepository empDocumentRepository;

    public List<Employee> getEmployeesByStoreId(Integer storeId) {
        List<Employee> employees = hrRepository.findByStoreId(storeId);
        System.out.println("Employees found for store ID " + storeId + ": " + employees.size());
        if (employees.isEmpty()) {
            System.out.println("No employees found for store ID " + storeId);
        } // 직원 수 로그 추가
        return employees;
    }

    // 신규 직원 등록
    @Transactional
    public Employee registerEmployee(EmployeeDTO employeeDTO, Integer storeId) {
        Employee employee = null;
        // storeId 로 스토어 조회
        try {
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new Exception500("서버 에러"));

            // bankId로 은행 조회
            Bank bank = bankRepository.findById(employeeDTO.getBankId())
                    .orElseThrow(() -> new Exception500("유효하지 않은 은행 ID"));

            // 현재 스토어에서 등록된 직원 중 가장 높은 storeEmployeeNumber 조회
            Integer maxStoreEmployeeNumber = hrRepository.findMaxStoreEmployeeNumberByStoreId(storeId);
            int newStoreEmployeeNumber = (maxStoreEmployeeNumber == null) ? 1 : maxStoreEmployeeNumber + 1;

            // 유니크한 사원 번호 생성
            String uniqueEmployeeNumber = storeId + "-" + newStoreEmployeeNumber;

            employee = Employee.builder()
                    .name(employeeDTO.getName())
                    .birthday(employeeDTO.getBirthday())
                    .gender(employeeDTO.getGender())
                    .email(employeeDTO.getEmail())
                    .phone(employeeDTO.getPhone())
                    .accountNumber(employeeDTO.getAccountNumber())
                    .address(employeeDTO.getAddress())
                    .position(employeeDTO.getPosition())
                    .store(store)
                    .bank(bank)
                    .storeEmployeeNumber(newStoreEmployeeNumber) // 새로운 사원번호 설정
                    .uniqueEmployeeNumber(uniqueEmployeeNumber) // 유니크 사원번호 설정
                    .employmentStatus(Employee.EmploymentStatus.ACTIVE) // 기본 상태
                    .build();

            // 직원 먼저 저장
            employee = hrRepository.save(employee);

            // 문서 정보 DTO로부터 EmpDocument 객체 생성
            EmpDocument empDocument = EmpDocument.builder()
                    .employee(employee) // 저장한 Employee와 연결
                    .employmentContract(employeeDTO.getEmpDocumentDTO().getEmploymentContract())
                    .healthCertificate(employeeDTO.getEmpDocumentDTO().getHealthCertificate())
                    .healthCertificateDate(employeeDTO.getEmpDocumentDTO().getHealthCertificateDate())
                    .identificationCopy(employeeDTO.getEmpDocumentDTO().getIdentificationCopy())
                    .bankAccountCopy(employeeDTO.getEmpDocumentDTO().getBankAccountCopy())
                    .residentRegistration(employeeDTO.getEmpDocumentDTO().getResidentRegistration())
                    .build();

            // 문서 저장
            empDocumentRepository.save(empDocument);
        } catch (Exception e) {
            // TODO 예외처리
            throw new RuntimeException("직원 등록 중 오류 발생: " + e.getMessage(), e);
        }

        return employee; // 저장된 Employee 객체 반환
    }


    // 직원 목록과 은행, 상점 정보를 가져오는 메서드
    public List<Employee> getAllEmployeesWithBankAndStore() {
        return hrRepository.findAllWithBankAndStore();
    }


    public List<BankDTO> getAllBankDTOs() {
        List<Bank> banks = bankRepository.findAll(); // 모든 은행 정보를 가져옴
        return banks.stream()
                .map(bank -> new BankDTO(bank.getId(), bank.getName())) // BankDTO로 변환
                .collect(Collectors.toList());
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .uniqueEmployeeNumber(employee.getUniqueEmployeeNumber())
                .name(employee.getName())
                .birthday(employee.getBirthday())
                .gender(employee.getGender())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .accountNumber(employee.getAccountNumber())
                .address(employee.getAddress())
                .position(employee.getPosition())
                .employmentStatus(employee.getEmploymentStatus())
                .bankId(employee.getBank() != null ? employee.getBank().getId() : null) // 은행 정보 추가
                .build();
    }

    // uniqueEmployeeNumber로(사원번호) 직원 정보를 조회
    public EmployeeDTO findByUniqueNumber(String uniqueEmployeeNumber) {
        Employee employee = hrRepository.findByUniqueEmployeeNumber(uniqueEmployeeNumber)
                .orElse(null);
        if (employee == null) {
            // 로깅 예제
            System.out.println("직원 ID " + uniqueEmployeeNumber + "을(를) 찾을 수 없습니다.");
        }
        return employee != null ? convertToDTO(employee) : null;
    }

    public EmpDocument getEmpDocumentsByEmployeeId(Integer employeeId) {
        return hrRepository.findByEmployeeId(employeeId);
    }

    public List<Employee> getEmployeesByStoreIdWithDocuments(Integer storeId) {
        return empDocumentRepository.findByStoreIdWithDocuments(storeId);
    }

}
