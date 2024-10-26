package com.uni.uni_erp.dto.erp.hr;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeUpdateDTO {
    private String name;
    private String birthday;
    private String address;
    private String phone;
    private Integer bankId;
    private String bankName;
    private String email;
    private String gender;
    private String accountNumber;
    private String employmentStatus;
    private Integer positionId;
    private Boolean employmentContract;
    private Boolean healthCertificate;
    private Boolean identificationCopy;
    private Boolean bankAccountCopy;
    private Boolean residentRegistration;
    private String healthCertificateDate;


}
