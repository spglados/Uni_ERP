package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Primary key for sales

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "order_num", nullable = false, unique = true)
    private Integer orderNum; // Unique identifier for the order, will be set using the sequence

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "sales_date", nullable = false)
    private LocalDateTime salesDate;

    @PrePersist
    protected void onPrePersist() {
        if (this.salesDate == null) {
            this.salesDate = LocalDateTime.from(Instant.now());
        }
    }

    public void generateOrderNum(EntityManager entityManager) {
        Integer sequenceValue = (Integer) entityManager.createNativeQuery("SELECT NEXT VALUE FOR order_num_seq").getSingleResult();
        this.orderNum = Integer.parseInt(String.format("%d%07d", storeId, sequenceValue));
    }
}
