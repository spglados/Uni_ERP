package com.uni.uni_erp.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Value
public class SalesDTO {
    private Integer id;
    private Integer orderNum;
    private Integer totalPrice;
    private LocalDateTime salesDate;
}
