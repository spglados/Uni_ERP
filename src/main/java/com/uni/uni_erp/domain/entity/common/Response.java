package com.uni.uni_erp.domain.entity.common;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name ="contact_response_tb")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "contact_id", nullable = false)
    private Integer contactId;

    @Column(name = "content", nullable = false)
    private String content;

}
