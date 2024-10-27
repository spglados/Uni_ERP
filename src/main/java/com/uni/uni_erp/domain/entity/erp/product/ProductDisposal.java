package com.uni.uni_erp.domain.entity.erp.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "product_disposal_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDisposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long productCode;

    @Column(nullable = false)
    private Integer amount;

    private LocalDate disposalDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Product product;

    @PrePersist
    protected void prePersist() {

        if (disposalDate == null) {
            disposalDate = LocalDate.now();
        }

    }

}
