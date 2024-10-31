package com.uni.uni_erp.domain.entity.erp.product;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "store_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_24_hours", nullable = false)
    private Integer is24Hours;

    @Column(name = "is_open", nullable = false)
    private Integer isOpen;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Product> products;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Employee> employees;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Material> materials;

    @Column(name = "allow_minutes",nullable = false)
    private Integer allowMinutes = 1;

    @Column(name = "store_address", nullable = false)
    private String storeAddress;
}
