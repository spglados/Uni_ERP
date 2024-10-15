package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.dto.product.IngredientDTO;
import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredient_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitCategory unit;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public IngredientDTO toIngredientDTO() {
        return IngredientDTO.builder()
                .id(this.id)
                .name(this.name)
                .amount(this.amount)
                .unit(this.unit.toString())
                .productId(this.product.getId())
                .build();
    }

}
