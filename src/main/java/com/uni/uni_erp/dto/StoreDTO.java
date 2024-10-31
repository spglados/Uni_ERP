package com.uni.uni_erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class StoreDTO {

    int id;
    String name;

    public StoreDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

}


