package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sales_detail_tb",
        indexes = {
                @Index(name = "idx_order_num", columnList = "order_num")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_code", nullable = false)
    private String itemCode;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SaleStatus status;

    @Column(name = "order_num", insertable = false, updatable = false)
    private Integer orderNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_num", referencedColumnName = "order_num", nullable = false)
    private Sales sales;

    @PrePersist
    protected void onPrePersist() {
        if (this.status == null) {
            this.status = SaleStatus.결제;
        }
    }

    public enum SaleStatus {
        결제,
        환불,
        취소
    }
}
