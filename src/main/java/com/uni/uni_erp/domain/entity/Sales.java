package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_code", nullable = false)
    private String itemCode;

    @Column(name = "specs")
    private String specs;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "tax", precision = 10, scale = 2, nullable = false)
    private BigDecimal tax;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "attachment_uri")
    private String attachmentUri;

    @Column(name = "sales_date", nullable = false)
    private LocalDate salesDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SaleStatus status;

//    @Column(name = "created_by", nullable = false)
//    private String createdBy;

    @PrePersist
    protected void onPrePersist() {
        if (this.status == null) {
            this.status = SaleStatus.CONFIRMED;
        }
        if (this.salesDate == null) {
            this.salesDate = LocalDate.now();
        }
    }


    public enum SaleStatus {
        CONFIRMED,
        REFUNDED,
        CANCELED
    }

}
