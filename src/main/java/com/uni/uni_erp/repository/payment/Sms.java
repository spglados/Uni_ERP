package com.uni.uni_erp.repository.payment;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sms_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private Integer randomNumber;
}
