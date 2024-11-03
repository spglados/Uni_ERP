package com.uni.uni_erp.domain.entity.erp.pos;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pos_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer posId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="pos_history_id")
    private PosHistory posHistory;

}
