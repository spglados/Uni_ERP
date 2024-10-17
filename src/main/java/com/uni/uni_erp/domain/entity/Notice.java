package com.uni.uni_erp.domain.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notice_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categorys category;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private int views;

    public enum Categorys{
        업데이트,
        안내
    }

    public String dateFormatter() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(createdAt);
    }

}
