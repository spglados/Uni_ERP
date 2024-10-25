package com.uni.uni_erp.dto;

import com.uni.uni_erp.domain.entity.User;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PrincipalDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private User.Membership membership;
    private Timestamp createdAt;
}
