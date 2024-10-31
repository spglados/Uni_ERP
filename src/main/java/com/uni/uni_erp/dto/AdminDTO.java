package com.uni.uni_erp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class AdminDTO {

    private String username;
    private String password;
    private String name;
    private String tel;
    private String email;


    @Builder
    @Getter
    public static class LoginDTO {

        private String username;
        private String password;

    }

}
