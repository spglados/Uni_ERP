package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

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

    @Column(nullable = false)
    @CreatedDate
    private LocalDate statusDate;  // 실자재 상태가 기록된 날짜

    private Double loss;  // 이론 재고와 실제 재고의 차이

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

}
