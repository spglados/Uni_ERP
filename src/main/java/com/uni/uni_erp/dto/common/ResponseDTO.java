package com.uni.uni_erp.dto.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {

    private String author;
    private Integer contactId;
    private String content;

}
