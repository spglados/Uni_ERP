package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

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

    private Double subAmount;

    @Enumerated(EnumType.STRING)
    private UnitCategory subUnit;

    private String supplier;

    private Timestamp receiptDate;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;
}
