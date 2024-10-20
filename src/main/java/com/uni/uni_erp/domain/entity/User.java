package com.uni.uni_erp.domain.entity;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
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
}
