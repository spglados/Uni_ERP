package com.uni.uni_erp.dto.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmpDocumentDTO {
    private Integer id; // 추가된 ID 필드
    private Boolean employmentContract;
    private Boolean healthCertificate;
    private String healthCertificateDate; // String으로 변환된 건강증명서 날짜
    private Boolean identificationCopy;
    private Boolean bankAccountCopy;
    private Boolean residentRegistration;

    // EmpDocument 엔티티를 EmpDocumentDTO로 변환하는 생성자
    public EmpDocumentDTO(EmpDocument empDocument) {
        this.id = empDocument.getId(); // ID 필드 초기화
        this.employmentContract = empDocument.getEmploymentContract() != null && empDocument.getEmploymentContract();
        this.healthCertificate = empDocument.getHealthCertificate() != null && empDocument.getHealthCertificate();
        // healthCertificateDate를 Timestamp에서 String으로 변환
        this.healthCertificateDate = empDocument.getHealthCertificateDate() != null
                ? DateFormatter.toDate(empDocument.getHealthCertificateDate()) : null;
        this.identificationCopy = empDocument.getIdentificationCopy() != null && empDocument.getIdentificationCopy();
        this.bankAccountCopy = empDocument.getBankAccountCopy() != null && empDocument.getBankAccountCopy();
        this.residentRegistration = empDocument.getResidentRegistration() != null && empDocument.getResidentRegistration();
    }

}
