package com.uni.uni_erp.dto;

import com.uni.uni_erp.domain.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    @Data
    public class loginDTO {
        private String email;
        private String password;
    }

    @Data
    public class JoinDTO {

        private String name;

        private String email;

        private String password;

        private String phone;

        private String address;

        private User.Role role; // 유저 역할 추가

        public User toUserEntity() {
            return User.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .phone(phone)
                    .address(address)
                    .role(role)
                    .build();
        }

    }

    public enum Role {
        OWNER,   // 사장님
        EMPLOYEE // 직원
    }


}
