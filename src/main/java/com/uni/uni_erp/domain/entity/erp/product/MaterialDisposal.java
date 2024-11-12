package com.uni.uni_erp.domain.entity.erp.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "material_disposal_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDisposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long materialCode;

    @Column(nullable = false)
    private Double amount;

    private LocalDate disposalDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Material material;

    @PrePersist
    protected void prePersist() {

        if (disposalDate == null) {
            disposalDate = LocalDate.now();
        }

    }

}
