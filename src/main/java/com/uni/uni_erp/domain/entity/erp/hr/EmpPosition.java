package com.uni.uni_erp.domain.entity.erp.hr;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Builder.Default
    private String name = "미정";

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String scheduleColor;

    @Column(name = "min_required_num",nullable = false)
    @Builder.Default
    private Integer minRequiredNum = 0;

    // TODO 오류 발생 시 삭제해야함
    @OneToMany(mappedBy = "empPosition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Employee> employees;
}
