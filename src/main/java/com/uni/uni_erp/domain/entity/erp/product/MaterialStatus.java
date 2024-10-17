package com.uni.uni_erp.domain.entity.erp.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "material_status_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double amount;

    private Double subAmount;

    private Double previousLossAmount;

    @Column(nullable = false)
    @CreatedDate
    private LocalDate statusDate;  // 자재 상태가 기록된 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @OneToMany(mappedBy = "status", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<MaterialOrder> orders;

    @PrePersist
    protected void onCreate() {
        if(statusDate == null) {
            statusDate = LocalDate.now();
        }
    }

}
