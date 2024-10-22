package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Table(name = "material_order_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Column(nullable = false)
    private LocalDate receiptDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    private LocalDate enterDate;

    private Boolean isUse;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id", nullable = false)
    private MaterialAdjustment adjustment;

    @PrePersist
    protected void onCreate() {

        if(enterDate == null) {
            enterDate = LocalDate.now();
        }

        if(isUse == null) {
            isUse = Boolean.TRUE;
        }

    }

}
