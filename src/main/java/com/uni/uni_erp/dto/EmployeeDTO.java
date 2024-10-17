package com.uni.uni_erp.dto;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.Position;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.lang.model.type.IntersectionType;
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
    private String accountNumber;
    private String position;
    private Integer storeId;
    private Integer storeEmployeeNumber; // 각 가게별로 증가하는 직원 번호 추가
    private String uniqueEmployeeNumber; // 사원 번호

    private Employee.EmploymentStatus employmentStatus;
    private EmpDocumentDTO empDocumentDTO; // 문서 정보 DTO

    private Timestamp hiredAt;
    private Timestamp updatedAt;
    private Timestamp quitAt;

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
        this.bankId = employee.getBank() != null ? employee.getBank().getId() : null; // 은행 ID
        this.accountNumber = employee.getAccountNumber();
        this.position = employee.getPosition();
        this.storeId = employee.getStore() != null ? employee.getStore().getId() : null; // 상점 ID
        this.storeEmployeeNumber = employee.getStoreEmployeeNumber();
        this.uniqueEmployeeNumber = employee.getUniqueEmployeeNumber();
        this.employmentStatus = employee.getEmploymentStatus();
        this.hiredAt = employee.getHiredAt();
        this.updatedAt = employee.getUpdatedAt();
        this.quitAt = employee.getQuitAt();

        // EmpDocumentDTO로 변환 (문서 정보가 없을 경우 빈 객체로 초기화)
        if (employee.getEmpDocuments() != null && !employee.getEmpDocuments().isEmpty()) {
            this.empDocumentDTO = new EmpDocumentDTO(employee.getEmpDocuments().get(0)); // 첫 번째 문서만 가져오기
        } else {
            this.empDocumentDTO = new EmpDocumentDTO(); // 빈 객체로 초기화
        }
    }

}
