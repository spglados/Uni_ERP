package com.uni.uni_erp.domain.entity.payment;

import com.uni.uni_erp.domain.entity.User;
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

    private String lastTransactionKey;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String billingKey;
    private String customerKey;
    private Integer amount;
    private String totalAmount;
    private String requestedAt;
    private String approvedAt;
    private String cancel;
    private Integer nowPayAmount;
    private Integer nextPayAmount;
    private String nextPay;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<PaymentHistory> paymentHistories;

}
