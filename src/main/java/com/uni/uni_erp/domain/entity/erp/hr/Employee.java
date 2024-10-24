package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.Bank;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.util.date.DateFormatter;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "employee_tb")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String birthday;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public enum Gender {
        M, //남자
        F // 여자
    }

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String address;

    @Column(unique = true, name = "account_number", nullable = false)
    private String accountNumber;
    // 사용자 정의 직책

    @Column(nullable = true)
    private String position;
    // 외래 키 설정: Employee는 하나의 Store에 속함

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 관리하는 사용자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // EmpDocument와의 관계
    private EmpDocument empDocument;

    @Column(nullable = false)  // Not Null 설정
    private Integer storeEmployeeNumber;  // 각 가게별로 증가하는 직원 번호

    @Column(unique = true, nullable = false)  // 고유한 사원번호, Not Null, 유니크 설정
    private String uniqueEmployeeNumber;


    public enum EmploymentStatus {
        ACTIVE, // 재직중
        INACTIVE, // 퇴사
        ONLEAVE // 휴직
    }

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "quit_at", updatable = false)
    private Timestamp quitAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "hired_at", nullable = true)
    private Timestamp hiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank; // 은행 정보를 참조 필드

    @PrePersist
    public void onCreate() {
        if (this.employmentStatus == null) {
            this.employmentStatus = EmploymentStatus.ACTIVE;
        }
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }


    public EmployeeDTO toEmployeeDTO() {
        return EmployeeDTO.builder()
                .id(this.id)
                .storeId(this.store.getId())
                .storeEmployeeNumber(storeEmployeeNumber)
                .name(this.name)
                .birthday(this.birthday)
                .gender(this.gender)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .accountNumber(this.accountNumber)
                .position(this.position)
                .bankId(this.bank.getId())
                .bankName(this.bank.getName())
                .uniqueEmployeeNumber(this.uniqueEmployeeNumber)
                .employmentStatus(this.employmentStatus)
                .hiredAt(Timestamp.from(Instant.now()))
                .employmentContract(this.empDocument.getEmploymentContract())
                .healthCertificate(this.empDocument.getHealthCertificate())
                .healthCertificateDate(this.empDocument != null && this.empDocument.getHealthCertificateDate() != null
                        ? DateFormatter.toDate(this.empDocument.getHealthCertificateDate())
                        : "보건증 갱신일을 입력해주세요") // 보건증 갱신일이 null일 경우 메시지 설정
                .residentRegistration(this.empDocument.getResidentRegistration())
                .bankAccountCopy(this.empDocument.getBankAccountCopy())
                .identificationCopy(this.empDocument.getIdentificationCopy())
                .build();
    }

}
