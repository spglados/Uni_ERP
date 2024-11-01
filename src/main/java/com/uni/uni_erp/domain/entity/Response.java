package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.maven.doxia.sink.Sink;

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
