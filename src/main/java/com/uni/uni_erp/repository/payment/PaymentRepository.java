package com.uni.uni_erp.repository.payment;

import com.uni.uni_erp.domain.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user.id = ?1 AND p.status = 1 AND p.status = -1")
    Integer sumAmountByUserId(Integer userId);


    List<Payment> findByUserId(Integer userPk);


    @Modifying
    @Query(value = "UPDATE Payment p SET p.cancel = 'Y' WHERE p.id = ?1")
    public void updateCancel(int payPk);

    //@Query("SELECT COUNT(p) FROM Payment p WHERE p.user.id = :userId AND p.status = 1 AND p.status = 2 AND p.status = -1")
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.user.id = :userId AND (p.status = 1 OR p.status = -1)")
    Integer countPaymentsByUserIdAndStatus(@Param("userId") Integer userId);

    @Modifying
    @Query(value = "UPDATE Payment p SET p.status = 1 WHERE p.id = (SELECT p2.id FROM Payment p2 WHERE p2.status = -1 ORDER BY p2.date DESC LIMIT 1)")
    void updateLatestPaymentStatusToOne();

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.cancel = 'N'")
    Integer countByCancelN(Integer userPk);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status <> 0 AND p.user.id = :userPk")
    Integer countByStatusNotZero(@Param("userPk") Integer userPk);




    /* @Query("UPDATE User u SET u.membership = :membership WHERE u.id = :userId")
    void updateMembership(Integer userId, User.Membership membership);*/
}
