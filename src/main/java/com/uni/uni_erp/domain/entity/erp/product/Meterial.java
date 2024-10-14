package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "meterial_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Double amount;

    private UnitCategory unit;

    private Double subAmount;

    private UnitCategory subUnit;

}
