package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "material_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // 유저 + 가게 + 아이디
    private Long materialCode;

    private String category;

    @Enumerated(EnumType.STRING)
    private UnitCategory unit;

    private Double subAmount;

    @Enumerated(EnumType.STRING)
    private UnitCategory subUnit;

    private LocalDate enterDate;

    private Double alarmCycle;

    @Enumerated(EnumType.STRING)
    private UnitCategory alarmUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MaterialOrder> orders;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MaterialAdjustment> statusHistory;

    @PrePersist
    protected void onCreate() {
        if (enterDate == null) {
            enterDate = LocalDate.now();
        }
    }

}
