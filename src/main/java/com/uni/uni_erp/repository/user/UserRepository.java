package com.uni.uni_erp.repository.user;

import com.uni.uni_erp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmailAndPassword(String email, String password);

    public boolean existsByEmail(String email);

    public boolean existsByPhone(String phone);

    // 구독자 수
    public int countByMembership(User.Membership membership);

    // 작년 구독자 수
    @Query("SELECT COUNT(u) FROM User u WHERE u.membership = :membership AND u.createdAt BETWEEN :startDate AND :endDate")
    int countByMembershipAndCreatedAtBetween(
            @Param("membership") User.Membership membership,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate
    );

    @Modifying
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :userId")
    void updateEmailByUserId(@Param("email") String email, @Param("userId") int userId);

    @Modifying
    @Query("UPDATE User u SET u.phone = :phone WHERE u.id = :userId")
    void updatePhoneByUserId(@Param("phone") String phone, @Param("userId") int userId);

    @Modifying
    @Query("UPDATE User u SET u.address = :address WHERE u.id = :userId")
    void updateAddressByUserId(@Param("address") String address, @Param("userId") int userId);

    @Modifying
    @Query("UPDATE User u SET u.paymentDate = :paymentDate WHERE u.id = :userId")
    void updatePaymentDateByUserId(@Param("paymentDate") String paymentDate, @Param("userId") int userId);

}
