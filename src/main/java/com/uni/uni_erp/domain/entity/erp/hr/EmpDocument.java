package com.uni.uni_erp.domain.entity.erp.hr;

import jakarta.annotation.ManagedBean;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "emp_document_tb")
public class EmpDocument {

    /**
     * 노무 관련 문서
     * true -> 제출된 서류
     * false -> 미제출 서류
     */

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    // 근로계약서
    @Column(nullable = false)
    private Boolean employmentContract;

    // 보건증
    @Column(nullable = false)
    private Boolean healthCertificate;

    // 보건증 유효기간
    @Column(nullable = false)
    private Timestamp healthCertificateDate;

    // 신분증사본
    @Column(nullable = false)
    private Boolean identificationCopy;

    // 통장사본
    @Column(nullable = false)
    private Boolean bankAccountCopy;

    // 주민등록등본
    @Column(nullable = false)
    private Boolean residentRegistration;

    @PrePersist
    public void prePersist() {
        employmentContract = employmentContract != null && employmentContract;
        healthCertificate = healthCertificate != null && healthCertificate;
        identificationCopy = identificationCopy != null && identificationCopy;
        bankAccountCopy = bankAccountCopy != null && bankAccountCopy;
        residentRegistration = residentRegistration != null && residentRegistration;
    }
}
