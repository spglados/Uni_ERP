package com.uni.uni_erp.domain.entity;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sales_refund_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesRefundDetail {

    @Id
    @GeneratedValue
    @Column(name = "sales_refund_detail_id")
    private Integer id;

    @Column(name = "item_code", nullable = false)
    private Long itemCode;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "order_num", insertable = false, updatable = false)
    private Integer orderNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status", nullable = false)
    private RefundStatus refundStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_num", referencedColumnName = "order_num", nullable = false)
    private Sales sales;

    private enum RefundStatus {
        취소, // Cancelled
        환불  // Refunded
    }

}
