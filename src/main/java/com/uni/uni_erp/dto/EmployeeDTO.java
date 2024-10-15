package com.uni.uni_erp.dto;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

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

}
