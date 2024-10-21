package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    private Integer id;
    private String name;
    private String birthday;
    private Timestamp createdAt;
    private Employee.Gender gender;
    private String email;
    private String phone;
    private String address;
    private Integer bankId; // 은행명
    private String bankName;
    private String accountNumber;
    private EmpPosition empPosition;
    private Integer storeId;
    private Integer storeEmployeeNumber; // 각 가게별로 증가하는 직원 번호 추가
    private String uniqueEmployeeNumber; // 사원 번호

    private Employee.EmploymentStatus employmentStatus;
    private EmpDocumentDTO empDocumentDTO; // 문서 정보 DTO

    private Timestamp hiredAt;
    private Timestamp updatedAt;
    private Timestamp quitAt;

    private boolean employmentContract;
    private boolean healthCertificate;
    private String healthCertificateDate; //보건 갱신날짜
    private boolean identificationCopy;
    private boolean bankAccountCopy;
    private boolean residentRegistration;

    //등록시 기본 값 초기화
    public EmpDocumentDTO getEmpDocumentDTO() {
        if (empDocumentDTO == null) {
            empDocumentDTO = new EmpDocumentDTO(); // 기본값으로 초기화
        }
        return empDocumentDTO;
    }



    // EmployeeDTO로 변환하는 생성자
    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.birthday = employee.getBirthday();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.address = employee.getAddress();
        // 은행 ID와 은행 이름 설정
        if (employee.getBank() != null) {
            this.bankId = employee.getBank().getId();
            this.bankName = employee.getBank().getName(); // 은행 이름 설정
        }
        this.accountNumber = employee.getAccountNumber();
        this.empPosition = employee.getEmpPosition();
        this.storeId = employee.getStore() != null ? employee.getStore().getId() : null; // 상점 ID
        this.storeEmployeeNumber = employee.getStoreEmployeeNumber();
        this.uniqueEmployeeNumber = employee.getUniqueEmployeeNumber();
        this.employmentStatus = employee.getEmploymentStatus();
        this.hiredAt = employee.getHiredAt();
        this.updatedAt = employee.getUpdatedAt();
        this.quitAt = employee.getQuitAt();
        //this.healthCertificateDate = employee.toEmployeeDTO().getHealthCertificateDate();

        // EmpDocumentDTO로 변환 (문서 정보가 없을 경우 빈 객체로 초기화)
        if (employee.getEmpDocument() != null) {
            this.empDocumentDTO = new EmpDocumentDTO(employee.getEmpDocument()); // EmpDocument 객체로 처리
        } else {
            this.empDocumentDTO = new EmpDocumentDTO(); // 빈 객체로 초기화
        }
    }


}
