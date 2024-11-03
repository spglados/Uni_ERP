package com.uni.uni_erp.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StoreUpdateDTO {
    private Integer id;
    private String name;
    private String storeAddress;
    private Integer is24Hours;

    public StoreUpdateDTO(Integer id, String name, String storeAddress, Integer is24Hours) {
        this.id = id;
        this.name = name;
        this.storeAddress = storeAddress;
        this.is24Hours = is24Hours;
    }
}
