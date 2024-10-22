package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.util.Str.UnitCategory;
import com.uni.uni_erp.util.date.PriceFormatter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

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
    @JoinColumn(name = "status_id", nullable = false)
    private MaterialAdjustment status;

    @PrePersist
    protected void onCreate() {

        if(enterDate == null) {
            enterDate = LocalDate.now();
        }

        if(isUse == null) {
            isUse = Boolean.TRUE;
        }

    }

    public MaterialDTO.MaterialOrderDTO toMaterialOrderDTO() {
        return MaterialDTO.MaterialOrderDTO.builder()
                .id(this.id)
                .name(this.name)
                .price(PriceFormatter.formatToPrice(this.price))
                .amount(this.amount)
                .unit(this.unit.toString())
                .supplier(this.supplier)
                .receiptDate(this.receiptDate)
                .expirationDate(this.expirationDate)
                .enterDate(this.enterDate)
                .isUse(this.isUse)
                .materialId(this.material.getId())
                .statusId(this.status.getId())
                .build();

    }
}
