package com.uni.uni_erp.domain.entity.payment;

import com.uni.uni_erp.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "payment_history_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 정기결제내역
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment; // 결제 정보를 연결

    private Integer amount; // 결제 금액
    private String status; // 결제 상태 (예: 성공, 실패)
    private String transactionId; // 거래 ID
    private String paymentMethod; // 결제 방법
    private String createdAt; // 생성 시간

    private Integer historyStatus;
}
