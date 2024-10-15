package com.uni.uni_erp.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmpDocumentDTO {

    private Integer employeeId; // 직원 ID
    private Boolean employmentContract; // 근로계약서 제출 여부
    private Boolean healthCertificate; // 보건증 제출 여부
    private Timestamp healthCertificateDate; // 보건증 유효기간
    private Boolean identificationCopy; // 신분증 사본 제출 여부
    private Boolean bankAccountCopy; // 통장 사본 제출 여부
    private Boolean residentRegistration; // 주민등록등본 제출 여부
}


