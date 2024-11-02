package com.uni.uni_erp.dto;

import com.uni.uni_erp.domain.entity.Contact;
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
    private Contact.ContactStatus status;
}
