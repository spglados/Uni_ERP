package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.dto.product.ProductDTO;
import jakarta.persistence.*;
        import lombok.*;

        import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // UserId + StoreId + pk 형식 유지
    private Long productCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer price;

    @Lob
    private Blob image;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    public ProductDTO toProductDTO() {
        return ProductDTO.builder()
                .id(this.id)
                .productCode(this.productCode)
                .name(this.name)
                .category(this.category)
                .price(this.price)
                .storeId(this.store.getId())
                .image(null)
                .description(this.description)
                .build();
    }

}
