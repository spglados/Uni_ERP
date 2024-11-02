package com.uni.uni_erp.domain.entity.erp.hr;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 또는 다른 전략을 사용할 수 있습니다.
    private Integer id; // 고유 식별자 필드

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    // 근로계약서
    @Column(nullable = false, name = "employment_contract")
    private Boolean employmentContract;

    // 보건증
    @Column(nullable = false, name = "health_certificate")
    private Boolean healthCertificate;

    // 보건증 유효기간
    @Column(nullable = true, name = "health_certificate_date")
    private Timestamp healthCertificateDate;

    // 신분증사본
    @Column(nullable = false, name = "identification_copy")
    private Boolean identificationCopy;

    // 통장사본
    @Column(nullable = false, name = "bank_account_copy")
    private Boolean bankAccountCopy;

    // 주민등록등본
    @Column(nullable = false, name = "resident_registration")
    private Boolean residentRegistration;

    @PrePersist
    public void prePersist() {
        employmentContract = employmentContract != null ? employmentContract : false;
        healthCertificate = healthCertificate != null ? healthCertificate : false;
        identificationCopy = identificationCopy != null ? identificationCopy : false;
        bankAccountCopy = bankAccountCopy != null ? bankAccountCopy : false;
        residentRegistration = residentRegistration != null ? residentRegistration : false;
    }

}
