package com.uni.uni_erp.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ContactDTO {

    private Integer id;
    private Integer userId;
    private String title;
    private String content;

}
