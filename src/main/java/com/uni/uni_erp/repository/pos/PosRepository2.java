package com.uni.uni_erp.repository.pos;

import com.uni.uni_erp.domain.entity.erp.pos.Pos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PosRepository2 extends JpaRepository<Pos, Integer> {

     @Modifying
     @Query("UPDATE Pos p SET p.amount = p.amount - :withdrawalAmount WHERE p.id = :posId AND p.amount >= :withdrawalAmount")
     int withdrawAmount(Integer posId, Long withdrawalAmount);

}
