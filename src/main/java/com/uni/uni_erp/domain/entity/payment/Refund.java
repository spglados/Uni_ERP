package com.uni.uni_erp.domain.entity.payment;

import com.uni.uni_erp.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refund_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lastTransactionKey;

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private String cancelReason;

    @Column(nullable = false)
    private String requestedAt;

    @Column(nullable = false)
    private String approvedAt;

    @Column(nullable = false)
    private String cancelAmount;

    private Integer adminId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
