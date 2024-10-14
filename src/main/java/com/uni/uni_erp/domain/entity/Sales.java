package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "sales_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Primary key for sales

    @Column(name = "order_num", nullable = false, unique = true)
    private Integer orderNum; // Unique identifier for the order, will be set using the sequence

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "sales_date", nullable = false)
    private Timestamp salesDate;

    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SalesDetail> details;

    @PrePersist
    protected void onPrePersist() {
        if (this.salesDate == null) {
            this.salesDate = Timestamp.from(Instant.now());
        }
    }

    public void generateOrderNum(EntityManager entityManager) {
        this.orderNum = (Integer) entityManager.createNativeQuery("SELECT NEXT VALUE FOR order_num_seq").getSingleResult();
    }
}
