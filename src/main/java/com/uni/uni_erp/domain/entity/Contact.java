package com.uni.uni_erp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name ="contact_tb")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private ContactStatus status;

    public enum ContactStatus {
        OPEN,
        CLOSED
    }

    @PrePersist
    private void setStatus() {
        this.status = ContactStatus.OPEN;
    }
}
