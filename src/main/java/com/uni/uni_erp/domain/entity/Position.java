package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "position_tb")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 각 사용자가 커스텀 직책을 가질 수 있도록 설정

}
