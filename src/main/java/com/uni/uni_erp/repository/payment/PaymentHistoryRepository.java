package com.uni.uni_erp.repository.payment;

import com.uni.uni_erp.domain.entity.payment.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {

    PaymentHistory findTopByUserIdOrderByCreatedAtDesc(Integer userId); // 최신 기록 조회

}
