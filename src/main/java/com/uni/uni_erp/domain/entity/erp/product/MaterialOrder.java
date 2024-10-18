package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_order_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer price;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private UnitCategory unit;

    private String supplier;

    @CreatedDate
    private LocalDateTime receiptDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    private Boolean isUse;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private MaterialStatus status;

    @PrePersist
    protected void onCreate() {
        if (receiptDate == null) {
            receiptDate = LocalDateTime.now();
        }

        if(isUse == null) {
            isUse = Boolean.TRUE;
        }

    }
}
