package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales_tb", indexes = {
        @Index(name = "idx_sales_date_store_id", columnList = "sales_date, store_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sales {

    @Transient
    @PersistenceContext
    EntityManager entityManager;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Primary key for sales

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "order_num", nullable = false, unique = true)
    private Integer orderNum;


    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "sales_date", nullable = false)
    private LocalDateTime salesDate;

    @PrePersist
    protected void generateOrderNum() {
        if (orderNum == null) {
            orderNum = (Integer) entityManager.createNativeQuery("SELECT NEXT VALUE FOR order_num_seq").getSingleResult();
        }
    }

}
