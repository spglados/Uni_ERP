package com.uni.uni_erp.domain.entity.payment;

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
    private String paymentKey;
    private String cancelReason;
    private String requestedAt;
    private String approvedAt;
    private String cancelAmount;
    private Integer adminId;

}
