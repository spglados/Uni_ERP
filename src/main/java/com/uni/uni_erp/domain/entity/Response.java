package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name ="contact_response_tb")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(name = "content", nullable = false)
    private String content;
}
