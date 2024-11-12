package com.uni.uni_erp.domain.entity.payment;

import com.uni.uni_erp.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "payment_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String lastTransactionKey;

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private String billingKey;

    @Column(nullable = false)
    private String customerKey;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String requestedAt;

    @Column(nullable = false)
    private String approvedAt;

    @Column(nullable = false)
    private String cancel;

    @Column(nullable = false)
    private Integer nowPayAmount;

    @Column(nullable = false)
    private Integer nextPayAmount;

    @Column(nullable = false)
    private String nextPay;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private String method;

    private String cancelReason;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<PaymentHistory> paymentHistories;

}
