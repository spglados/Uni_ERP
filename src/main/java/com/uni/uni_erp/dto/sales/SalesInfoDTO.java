package com.uni.uni_erp.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesInfoDTO {
    private Integer time;
    private String amount;

    // 추가 생성자: 시간대와 매출액을 받아서 문자열로 변환
    public SalesInfoDTO(Integer hour, Long totalAmount) {
        this.time = hour;
        this.amount = "₩" + String.format("%,d", totalAmount);
    }
}
