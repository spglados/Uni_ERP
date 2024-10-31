package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "emp_position_tb")
public class EmpPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String scheduleColor;

    @Column(name = "min_required_num",nullable = false)
    private Integer minRequiredNum = 0;

    @PrePersist
    public void setDefaultValues() {
        if (this.name == null || this.name.isEmpty()) {
            this.name = "미정"; // 기본 값 설정
        }
    }

}
