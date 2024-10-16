package com.uni.uni_erp.dto;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
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

    // EmpDocument를 DTO로 변환하는 생성자
    public EmpDocumentDTO(EmpDocument empDocument) {
        this.employeeId = empDocument.getId();
        this.employmentContract = empDocument.getEmploymentContract();
        this.healthCertificate = empDocument.getHealthCertificate();
        this.healthCertificateDate = empDocument.getHealthCertificateDate();
        this.identificationCopy = empDocument.getIdentificationCopy();
        this.bankAccountCopy = empDocument.getBankAccountCopy();
        this.residentRegistration = empDocument.getResidentRegistration();
    }


}


