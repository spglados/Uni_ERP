package com.uni.uni_erp.dto.store;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStoreUpdateDTO {

    private Integer id;
    private String name;
    private Integer is24Hours;
    private Integer isOpen;
}
