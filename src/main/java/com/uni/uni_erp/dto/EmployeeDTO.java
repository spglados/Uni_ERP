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
    private String accountNumber;
    private String position;
    private Integer storeId;
    private Employee.EmploymentStatus employmentStatus;

}
