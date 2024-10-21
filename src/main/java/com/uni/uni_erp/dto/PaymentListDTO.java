package com.uni.uni_erp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class PaymentListDTO {
    private Integer id;
    private String orderId;
    private String orderName;
    private String paymentKey;
    private String billingKey;
    private String requestedAt;
    private String approvedAt;
    private String totalAmount;
    private String cancel;
}
