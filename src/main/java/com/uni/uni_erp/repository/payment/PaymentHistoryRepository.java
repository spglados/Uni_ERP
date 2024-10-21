package com.uni.uni_erp.repository.payment;

import com.uni.uni_erp.domain.entity.payment.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {

    PaymentHistory findTopByUserIdOrderByCreatedAtDesc(Integer userId); // 최신 기록 조회

    @Query("SELECT ph.amount FROM PaymentHistory ph WHERE ph.user.id = :userId ORDER BY ph.createdAt DESC Limit 1")
    Optional<Integer> findLatestAmountByUserId(@Param("userId") Integer userId);


}
