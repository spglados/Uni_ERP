package com.uni.uni_erp.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ContactDTO {

    private Integer id;
    private String name;
    private String email;
    private String tel;
    private String content;

}
