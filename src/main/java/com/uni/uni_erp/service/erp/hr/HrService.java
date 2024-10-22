package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.Bank;
import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.erp.hr.EmpDocumentDTO;
import com.uni.uni_erp.dto.erp.hr.EmployeeDTO;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.repository.bank.BankRepository;
import com.uni.uni_erp.repository.erp.hr.EmpDocumentRepository;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HrService {

    private final EmployeeRepository employeeRepository;
    private final StoreRepository storeRepository;
    private final BankRepository bankRepository;
    private final EmpDocumentRepository empDocumentRepository;

    // 스토어 ID로 직원 목록 조회
    public List<EmployeeDTO> getEmployeesByStoreId(Integer storeId) {
        return employeeRepository.findEmployeesByStoreId(storeId);
    }

    // 직원 상세 정보 조회
    public EmployeeDTO getEmployeeDetails(Integer employeeId) {
        return employeeRepository.findEmployeeWithAllDetails(employeeId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new Exception404("직원 정보를 찾을 수 없습니다."));
    }

    // 신규 직원 등록
    @Transactional
    public Employee registerEmployee(EmployeeDTO employeeDTO, Integer storeId) {
        Store store = getStoreById(storeId);
        Bank bank = getBankById(employeeDTO.getBankId());
        Integer newStoreEmployeeNumber = getNewStoreEmployeeNumber(storeId);

        Employee employee = buildEmployee(employeeDTO, store, bank, newStoreEmployeeNumber);
        employee = employeeRepository.save(employee);

        EmpDocument empDocument = buildEmpDocument(employeeDTO.getEmpDocumentDTO(), employee);
        empDocumentRepository.save(empDocument);

        return employee;
    }

    // 모든 직원과 은행 정보 조회
    public List<EmployeeDTO> getAllEmployeesWithBankAndStore() {
        return employeeRepository.findAllWithBankAndStore().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 모든 은행 DTO 조회
    public List<BankDTO> getAllBankDTOs() {
        return bankRepository.findAll().stream()
                .map(bank -> new BankDTO(bank.getId(), bank.getName()))
                .collect(Collectors.toList());
    }

    // 직원의 문서 정보 조회
    public EmpDocumentDTO getEmpDocumentsByEmployeeId(Integer employeeId) {
        return empDocumentRepository.findByEmployeeId(employeeId)
                .map(EmpDocumentDTO::new)
                .orElse(null);
    }

    private Store getStoreById(Integer storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new Exception500("유효하지 않은 상점 ID"));
    }

    private Bank getBankById(Integer bankId) {
        return bankRepository.findById(bankId)
                .orElseThrow(() -> new Exception500("유효하지 않은 은행 ID"));
    }

    private Integer getNewStoreEmployeeNumber(Integer storeId) {
        Integer maxStoreEmployeeNumber = employeeRepository.findMaxStoreEmployeeNumberByStoreId(storeId);
        return (maxStoreEmployeeNumber == null) ? 1 : maxStoreEmployeeNumber + 1;
    }

    private Employee buildEmployee(EmployeeDTO employeeDTO, Store store, Bank bank, Integer newStoreEmployeeNumber) {
        return Employee.builder()
                .name(employeeDTO.getName())
                .birthday(employeeDTO.getBirthday())
                .gender(employeeDTO.getGender())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .accountNumber(employeeDTO.getAccountNumber())
                .address(employeeDTO.getAddress())
                .empPosition(employeeDTO.getEmpPosition() != null ? employeeDTO.getEmpPosition() : null)
                .store(store)
                .bank(bank)
                .storeEmployeeNumber(newStoreEmployeeNumber)
                .uniqueEmployeeNumber(store.getId() + "-" + newStoreEmployeeNumber)
                .employmentStatus(Employee.EmploymentStatus.ACTIVE)
                .build();
    }

    private EmpDocument buildEmpDocument(EmpDocumentDTO empDocumentDTO, Employee employee) {
        return EmpDocument.builder()
                .employee(employee)
                .employmentContract(empDocumentDTO.getEmploymentContract() != null && empDocumentDTO.getEmploymentContract()) // 수정
                .healthCertificate(empDocumentDTO.getHealthCertificate() != null && empDocumentDTO.getHealthCertificate()) // 수정
                .healthCertificateDate(empDocumentDTO.getHealthCertificateDate() != null
                        ? Timestamp.valueOf(empDocumentDTO.getHealthCertificateDate()) : null)
                .identificationCopy(empDocumentDTO.getIdentificationCopy() != null && empDocumentDTO.getIdentificationCopy()) // 수정
                .bankAccountCopy(empDocumentDTO.getBankAccountCopy() != null && empDocumentDTO.getBankAccountCopy()) // 수정
                .residentRegistration(empDocumentDTO.getResidentRegistration() != null && empDocumentDTO.getResidentRegistration()) // 수정
                .build();
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .uniqueEmployeeNumber(employee.getUniqueEmployeeNumber())
                .name(employee.getName())
                .birthday(employee.getBirthday())
                .gender(employee.getGender())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .accountNumber(employee.getAccountNumber())
                .address(employee.getAddress())
                .empPosition(employee.getEmpPosition())
                .employmentStatus(employee.getEmploymentStatus())
                .bankId(employee.getBank() != null ? employee.getBank().getId() : null)
                .build();

        // EmpDocumentDTO 정보 추가
        if (employee.getEmpDocument() != null) {
            EmpDocumentDTO empDocumentDTO = new EmpDocumentDTO(employee.getEmpDocument());
            employeeDTO.setEmpDocumentDTO(empDocumentDTO); // employeeDTO를 통해 설정
        }

        return employeeDTO; // employeeDTO 반환
    }
}
