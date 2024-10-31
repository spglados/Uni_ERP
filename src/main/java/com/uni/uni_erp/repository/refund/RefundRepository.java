package com.uni.uni_erp.repository.refund;

import com.uni.uni_erp.domain.entity.payment.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Integer> {

    List<Refund> findByUserId(Integer userPk);

    @Query("SELECT SUM(CAST(p.cancelAmount AS LONG)) FROM Refund p")
    Long findAllSumRefund();
}
