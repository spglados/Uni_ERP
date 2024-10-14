package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.Position;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.Store;
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

    public enum EmploymentStatus {
        ACTIVE, // 재직중
        INACTIVE, // 퇴사
        ONLEAVE // 휴직
    }

    @Column(name = "hired_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "quit_at", updatable = false)
    private Timestamp quitAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EmpDocument> empDocuments;

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
}
