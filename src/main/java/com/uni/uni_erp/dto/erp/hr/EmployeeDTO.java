package com.uni.uni_erp.dto.erp.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ToString
public class EmployeeDTO {
    private Integer id;
    private String name;
    private String birthday;
    private Timestamp createdAt;
    private Employee.Gender gender;
    private String email;
    private String phone;
    private String address;
    private Integer bankId; // 은행 ID
    private String bankName; // 은행명
    private String accountNumber;
    @JsonIgnore
    private EmpPosition empPosition;
    private String empPositionName; // 직책 이름 추가
    private Integer storeId;
    private Integer storeEmployeeNumber; // 각 가게별로 증가하는 직원 번호 추가
    private long uniqueEmployeeNumber; // 유니크 사원 번호

    private Employee.EmploymentStatus employmentStatus;
    @JsonIgnore
    private EmpDocumentDTO empDocumentDTO; // 문서 정보 DTO

    private Timestamp hiredAt;
    private Timestamp updatedAt;
    private Timestamp quitAt;

    private boolean employmentContract;
    private boolean healthCertificate;
    private String healthCertificateDate; // 보건 갱신 날짜
    private boolean identificationCopy;
    private boolean bankAccountCopy;
    private boolean residentRegistration;

    // Employee 엔티티를 DTO로 변환하는 생성자
    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.birthday = employee.getBirthday();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.address = employee.getAddress();
        this.gender = employee.getGender(); // Employee의 gender 값을 직접 가져오는지 확인
        if (employee.getBank() != null) {
            this.bankId = employee.getBank().getId();
            this.bankName = employee.getBank().getName();
        }
        this.empPositionName = employee.getEmpPosition() != null ? employee.getEmpPosition().getName() : null; // 직책 이름 추가
        this.accountNumber = employee.getAccountNumber();
        this.empPosition = employee.getEmpPosition();
        this.storeId = employee.getStore() != null ? employee.getStore().getId() : null;
        this.storeEmployeeNumber = employee.getStoreEmployeeNumber();
        this.uniqueEmployeeNumber = employee.getUniqueEmployeeNumber();
        this.employmentStatus = employee.getEmploymentStatus();
        this.hiredAt = employee.getHiredAt();
        this.updatedAt = employee.getUpdatedAt();
        this.quitAt = employee.getQuitAt();

        // EmpDocumentDTO 설정
        if (employee.getEmpDocument() != null) {
            this.empDocumentDTO = new EmpDocumentDTO(employee.getEmpDocument());
            this.employmentContract = this.empDocumentDTO.getEmploymentContract();
            this.healthCertificate = this.empDocumentDTO.getHealthCertificate();
            this.healthCertificateDate = this.empDocumentDTO.getHealthCertificateDate();
            this.identificationCopy = this.empDocumentDTO.getIdentificationCopy();
            this.bankAccountCopy = this.empDocumentDTO.getBankAccountCopy();
            this.residentRegistration = this.empDocumentDTO.getResidentRegistration();
        }
    }

    // EmpDocumentDTO를 설정하는 메서드
    public void setEmpDocumentDTO(EmpDocumentDTO empDocumentDTO) {
        this.empDocumentDTO = empDocumentDTO;
    }


}