package com.uni.uni_erp.domain.entity.payment;

import com.uni.uni_erp.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "user_pay_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer totalAmount;

    private String nextPayDate;
    private String firstPayAmount;
    private String secondPayAmount;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
