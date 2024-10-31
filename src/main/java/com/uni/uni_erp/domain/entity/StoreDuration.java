package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "store_duration_tb")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "start_time", nullable = false)
    private Timestamp start;

    @Column(name = "end_time")
    private Timestamp end;

}
