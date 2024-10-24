package com.uni.uni_erp.repository.payment;

import com.uni.uni_erp.domain.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user.id = ?1")
    Integer sumAmountByUserId(Integer userId);


    List<Payment> findByUserId(Integer userPk);


    @Modifying
    @Query(value = "UPDATE Payment p SET p.cancel = 'Y' WHERE p.id = ?1")
    public void updateCancel(int payPk);


    /* @Query("UPDATE User u SET u.membership = :membership WHERE u.id = :userId")
    void updateMembership(Integer userId, User.Membership membership);*/
}
