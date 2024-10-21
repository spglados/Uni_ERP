package com.uni.uni_erp.domain.entity.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefundRepository extends JpaRepository<Refund, Integer> {

    @Query(value = "UPDATE Payment SET cancel = 'Y' WHERE id = ?1")
    public int update(int payPk);

}
