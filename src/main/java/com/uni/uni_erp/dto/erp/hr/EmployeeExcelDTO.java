package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeExcelDTO {
    private Integer id;
    private String name;
    private String birthday;
    private Employee.Gender gender;
    private String email;
    private String phone;
    private String address;
    private String bankName;
    private String accountNumber;
    private String empPositionName;
    private String employmentStatus;
    private long uniqueEmployeeNumber; // 사원 번호

    // 문서 관련 필드 추가
    private boolean employmentContract;
    private boolean healthCertificate;
    private String healthCertificateDate;
    private boolean identificationCopy;
    private boolean bankAccountCopy;
    private boolean residentRegistration;

    public EmployeeExcelDTO(EmployeeDTO employeeDTO) {
        this.id = employeeDTO.getId();
        this.name = employeeDTO.getName();
        this.birthday = employeeDTO.getBirthday();
        this.gender = employeeDTO.getGender();
        this.email = employeeDTO.getEmail();
        this.phone = employeeDTO.getPhone();
        this.address = employeeDTO.getAddress();
        this.bankName = employeeDTO.getBankName();
        this.accountNumber = employeeDTO.getAccountNumber();
        this.empPositionName = employeeDTO.getEmpPositionName();
        this.employmentStatus = employeeDTO.getEmploymentStatus().name();
        this.uniqueEmployeeNumber = employeeDTO.getUniqueEmployeeNumber();

        // 문서 관련 정보 설정
        if (employeeDTO.getEmpDocumentDTO() != null) {
            this.employmentContract = employeeDTO.getEmpDocumentDTO().getEmploymentContract();
            this.healthCertificate = employeeDTO.getEmpDocumentDTO().getHealthCertificate();
            this.healthCertificateDate = employeeDTO.getEmpDocumentDTO().getHealthCertificateDate();
            this.identificationCopy = employeeDTO.getEmpDocumentDTO().getIdentificationCopy();
            this.bankAccountCopy = employeeDTO.getEmpDocumentDTO().getBankAccountCopy();
            this.residentRegistration = employeeDTO.getEmpDocumentDTO().getResidentRegistration();
        }
    }

}
