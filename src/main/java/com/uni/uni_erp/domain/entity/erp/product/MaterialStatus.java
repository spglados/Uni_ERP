package com.uni.uni_erp.domain.entity.erp.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "material_status_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String unit;

    private Double subAmount;

    private String subUnit;

    private Double previousLossAmount;

}
