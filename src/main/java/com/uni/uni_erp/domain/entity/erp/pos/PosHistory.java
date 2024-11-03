package com.uni.uni_erp.domain.entity.erp.pos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "pos_history_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PosHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "posHistory", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Pos> pos;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Long adjustedAmount;

    @Column(nullable = false)
    private Integer status;

}
