package com.uni.uni_erp.domain.entity;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.domain.entity.payment.PaymentHistory;
import com.uni.uni_erp.domain.entity.payment.UserPay;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Membership membership;

    // created_at 컬럼과 매핑하며, 데이터 저장시 자동으로 현재 시간이 설정됨
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Store> stores;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Payment> payments;

    @OneToOne(mappedBy = "user")
    private UserPay userPay;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PaymentHistory> paymentHistories;

    // 추가된 결제일 컬럼
    @Column(name = "payment_date")
    private String paymentDate;

    // 멤버십 상태 변경 날짜
    private String previousMembership; // 이전 멤버십 상태
    private LocalDateTime premiumToCommonDate; // Premium에서 Common으로 변경된 날짜


    // 엔티티가 저장되기 전 실행되는 메서드
    @PrePersist
    protected void onPrePersist() {
        if (this.membership == null) {
            this.membership = Membership.COMMON;  // 기본값 설정
        }
        this.createdAt = Timestamp.from(Instant.now());  // 현재 시간을 createdAt에 설정
    }

    public enum Membership {
        COMMON,
        PREMIUM
    }

    public void setMembership(Membership membership) {
        this.membership = membership;

        // Premium에서 Common으로 변경될 때 날짜 기록
        if (Membership.COMMON.equals(membership)) {
            this.premiumToCommonDate = LocalDateTime.now();
        }
    }

    public boolean isWithinWeekOfCommon() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current time: " + now);
        System.out.println("premiumToCommonDate: " + premiumToCommonDate);
        // 이넘 타입으로 비교
        return Membership.COMMON.equals(membership) && premiumToCommonDate != null &&
                premiumToCommonDate.isAfter(LocalDateTime.now().minusWeeks(7));
    }
}
