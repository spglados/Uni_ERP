package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.Bank;
import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.erp.hr.*;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.repository.bank.BankRepository;
import com.uni.uni_erp.repository.erp.hr.EmpDocumentRepository;
import com.uni.uni_erp.repository.erp.hr.EmpPositionRepository;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import com.uni.uni_erp.util.ExcelUtil.EmpExcelUtil;
import com.uni.uni_erp.util.ExcelUtil.ExcelUtil;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HrService {

    private final EmployeeRepository employeeRepository;
    private final StoreRepository storeRepository;
    private final BankRepository bankRepository;
    private final EmpDocumentRepository empDocumentRepository;
    private final EmpPositionRepository empPositionRepository;
    private final ExcelUtil excelUtil;

    // 엑셀 다운로드
    public void downloadEmployeeExcel(HttpServletResponse response) {
        // 직원 정보 리스트 가져오기
        List<EmployeeDTO> employees = employeeRepository.findAll().stream()
                .map(EmployeeDTO::new) // Employee를 EmployeeDTO로 변환
                .collect(Collectors.toList());

        // Excel DTO 리스트로 변환
        List<EmployeeExcelDTO> excelEmployees = employees.stream()
                .map(EmployeeExcelDTO::new)
                .collect(Collectors.toList());

        // 엑셀 파일 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        excelUtil.createHeader(headerRow);
        // EmpExcelUtil.createHeader(headerRow); // 유틸리티 클래스의 메서드 호출

        // 데이터 추가
        for (int i = 0; i < excelEmployees.size(); i++) {
            Row row = sheet.createRow(i + 1);
            EmployeeExcelDTO employeeExcelDTO = excelEmployees.get(i);
            excelUtil.fillRow(row, employeeExcelDTO); // 유틸리티 클래스의 메서드 호출
        }

        // 응답 설정
        response.setContentType("application/octet-stream");
        String fileName = "employees_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 엑셀 파일 다운로드
        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeUpdateDTO employeeDTO) {
        try {

            // 직원 id로 엔티티 조회
            Employee employeeEntity = employeeRepository.findByUniqueEmployeeNumber(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            EmpDocument empDocumentEntity = empDocumentRepository.findByEmployeeId(employeeEntity.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            // 수정할 필드 업데이트
            employeeEntity.setName(employeeDTO.getName());
            employeeEntity.setBirthday(employeeDTO.getBirthday());
            employeeEntity.setGender(EnumCommonUtil.getEnumFromString(Employee.Gender.class, employeeDTO.getGender()));
            // 이메일 아이디와 도메인을 조합하여 이메일 설정
            String fullEmail = employeeDTO.getEmail() + "@" + employeeDTO.getEmailDomain();
            employeeEntity.setEmail(fullEmail);
            employeeEntity.setPhone(employeeDTO.getPhone());
            employeeEntity.setAddress(employeeDTO.getAddress());
            employeeEntity.setAccountNumber(employeeDTO.getAccountNumber());
            employeeEntity.setEmploymentStatus(EnumCommonUtil.getEnumFromString(Employee.EmploymentStatus.class, employeeDTO.getEmploymentStatus()));
            employeeEntity.setEmpPosition(empPositionRepository.findById(employeeDTO.getPositionId()).orElseThrow(() -> new RuntimeException("Employee not found")));
            employeeEntity.setBank(bankRepository.findById(employeeDTO.getBankId()).orElseThrow(() -> new RuntimeException("Bank not found")));
            empDocumentEntity.setEmploymentContract(employeeDTO.getEmploymentContract() != null);
            empDocumentEntity.setHealthCertificate(employeeDTO.getHealthCertificate() != null);
            empDocumentEntity.setIdentificationCopy(employeeDTO.getIdentificationCopy() != null);
            empDocumentEntity.setBankAccountCopy(employeeDTO.getBankAccountCopy() != null);
            empDocumentEntity.setResidentRegistration(employeeDTO.getResidentRegistration() != null);
            if (employeeDTO.getHealthCertificateDate() != null && !employeeDTO.getHealthCertificateDate().isEmpty()) {
                LocalDate localDate = LocalDate.parse(employeeDTO.getHealthCertificateDate());
                empDocumentEntity.setHealthCertificateDate(Timestamp.valueOf(localDate.atStartOfDay()));
            }
            employeeEntity.setEmpDocument(empDocumentEntity);
            employeeRepository.save(employeeEntity);
            return new EmployeeDTO(employeeEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


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

    public List<EmpPositionDTO> getPositionsByStoreId(Integer storeId) {
        List<EmpPosition> positions = empPositionRepository.findByStoreId(storeId);
        return positions.stream()
                .map(EmpPositionDTO::new) // EmpPosition을 EmpPositionDTO로 변환
                .collect(Collectors.toList());
    }

    // 신규 직원 등록
    @Transactional
    public Employee registerEmployee(EmployeeDTO employeeDTO, Integer storeId, Integer sessionUserId) {
        Store store = getStoreById(storeId);
        Bank bank = getBankById(employeeDTO.getBankId());
        Integer newStoreEmployeeNumber = getNewStoreEmployeeNumber(storeId);


        Employee employee = buildEmployee(employeeDTO, sessionUserId, store, bank, newStoreEmployeeNumber);
        employee = employeeRepository.save(employee);

        EmpDocument empDocument = buildEmpDocument(employeeDTO.getEmpDocumentDTO(), employee);
        empDocumentRepository.save(empDocument);

        return employee;
    }


    // 직원 ID로 직원 정보 가져오기
    public EmployeeDTO getEmployeeById(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("직원 정보를 찾을 수 없습니다."));
        return new EmployeeDTO(employee); // Employee를 EmployeeDTO로 변환하여 반환
    }

    // 중복 이메일 검사
    public boolean isEmailDuplicated(String email) {
        return employeeRepository.existsByEmail(email);
    }

    // 중복 전화번호 검사
    public boolean isPhoneDuplicated(String phone) {
        return employeeRepository.existsByPhone(phone);
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

    private Employee buildEmployee(EmployeeDTO employeeDTO, Integer sessionUserId, Store store, Bank bank, Integer newStoreEmployeeNumber) {
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
                .uniqueEmployeeNumber(Long.parseLong((sessionUserId + "" + store.getId() + "" + newStoreEmployeeNumber)))
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

