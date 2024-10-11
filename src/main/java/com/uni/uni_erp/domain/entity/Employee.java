package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@NoArgsConstructor
@Data
@Entity
@Table(name = "employee_tb")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String birthday;


    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true,nullable = false)
    private String phone;

    @Column(unique = true, name = "account_number", nullable = false)
    private String accountNumber;


    // 외래 키 설정: Employee는 하나의 Position에 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    // 외래 키 설정: Employee는 하나의 User에 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner; // 관리하는 사용자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;



    public enum Gender{
        M, //남자
        F // 여자
    }


    public enum EmploymentStatus{
        ACTIVE, // 재직중
        INACTIVE, // 퇴사
        ONLEAVE // 휴직
    }

    @PrePersist
    protected void onCreate(){
        if(this.employmentStatus == null){
            this.employmentStatus = EmploymentStatus.ACTIVE;
        }
        this.createdAt = Timestamp.from(Instant.now());
    }
}
