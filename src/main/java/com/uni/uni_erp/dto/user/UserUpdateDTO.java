package com.uni.uni_erp.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String membership;

}
