package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.dto.product.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // UserId + StoreId + pk 형식 유지
    @Column(nullable = false)
    private Integer productCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ingredient> ingredients;

    public ProductDTO toProductDTO() {
        return ProductDTO.builder()
                .id(this.id)
                .productCode(this.productCode)
                .name(this.name)
                .category(this.category)
                .price(this.price)
                .storeId(this.store.getId())
                .build();
    }

}
