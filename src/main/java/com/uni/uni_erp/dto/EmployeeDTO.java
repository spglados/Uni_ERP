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
    private Integer bankId;
    private String accountNumber;
    private String position;
    private Integer storeId;
    private Employee.EmploymentStatus employmentStatus;
    private List<EmpDocument> empDocuments;

    private Timestamp hiredAt;
    private Timestamp updatedAt;
    private Timestamp quitAt;

}
