package com.uni.uni_erp.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false) //TODO 필요한가
@Builder
public class PaymentHistoryDTO {
    private Integer id;
    private String paymentKey; // 결제 키 (결제 승인, 조회, 취소 API에 사용)
    private String type; // 결제 타입 (일반, 자동, 브랜드페이)
    private Integer userId; // 주문고객 이름
    private String orderId; // 주문번호 (결제 요청시 서버에서 만들어서 사용한 값)
    private String orderName; // 구매상품 이름
    private String method; // 결제 수단(카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, ...)
    private Long totalAmount; // 총 결제 금액 (최초 결제 금액 유지)
    private String requestedAt; // 결제 날짜, 시간
    private String approvedAt; // 결제 승인 날짜, 시간
    private String status; // 결제 처리 상태

    private Long cancelAmount; // 취소 금액
    private Timestamp canceledAt; // 취소 시간
    private String cancelReason; // 취소 사유
    private String cancelStatus; // 취소 처리 상태
}
