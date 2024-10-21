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
    @NoArgsConstructor
    public static class JoinDTO {

        private String name;

        private String email;

        private String password;

        private String phone;

        private String address;

        public User toUserEntity() {
            return User.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .phone(phone)
                    .address(address).build();
        }

    }



}
