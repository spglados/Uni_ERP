package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "material_adjustment_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double theoreticalAmount;

    private Double actualAmount;

    @Enumerated(EnumType.STRING)
    private UnitCategory unit;

    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

}
