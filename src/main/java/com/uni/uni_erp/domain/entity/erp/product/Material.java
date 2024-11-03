package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "material_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MaterialOrder> orders;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MaterialAdjustment> adjustmentHistory;

    // TODO 오류 발생시 이부분 삭제 해야됨
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Ingredient> ingredients;

    // TODO 오류 발생시 이부분 삭제 해야됨
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MaterialStatus> materialStatuses;

    // TODO 오류 발생시 이부분 삭제 해야됨
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MaterialDisposal> materialDisposals;

    @PrePersist
    protected void prePersist() {
        if (enterDate == null) {
            enterDate = LocalDate.now();
        }

        // 표준 단위 변환 설정
        if (unit == UnitCategory.KG && subUnit == UnitCategory.G) {
            subAmount = 1000.0;
        } else if (unit == UnitCategory.G && subUnit == UnitCategory.KG) {
            subAmount = 0.001;
        } else if (unit == UnitCategory.L && subUnit == UnitCategory.ML) {
            subAmount = 1000.0;
        } else if (unit == UnitCategory.ML && subUnit == UnitCategory.L) {
            subAmount = 0.001;
        }
    }

}
