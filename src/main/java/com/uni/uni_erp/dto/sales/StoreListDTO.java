package com.uni.uni_erp.dto.sales;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class StoreListDTO {
    private Integer id;
    private String name;
    private Integer is24Hours;
    private Integer isOpen;
    private Timestamp createdAt;
    private String userName;
}
