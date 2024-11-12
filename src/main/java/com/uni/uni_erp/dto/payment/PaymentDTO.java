package com.uni.uni_erp.dto.payment;

import com.uni.uni_erp.domain.entity.user.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.domain.entity.payment.Refund;
import lombok.*;

import javax.smartcardio.Card;

@Data
@Builder
@ToString
public class PaymentDTO {

    public PaymentDTO() {}

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class RegularPaymentDTO {
        private Integer userId;
        private String customerKey;
        private String billingKey;
        private Integer amount;
        private String orderName;
        private String method;

        // 결제 완료 후 받아오는 값
        private String lastTransactionKey;
        private String paymentKey;
        private String orderId;
        private String requestedAt;
        private String approvedAt;
        private String totalAmount;
        private String cancel;
        private Card card;

        // 결제 취소 시 입력되는 값
        private String cancelReason;
        private String cancelAmount;
        private Integer adminId;

        // 다음 정기결제일 계산
        private String nextPay;
        // 다음 정기 결제금액
        private Integer nowPayAmount;
        private Integer nextPayAmount;

        //결제한 날짜
        private String date;
        private Integer status;
        // 에러 메시지
        private Integer billingErrorCode;
        private Integer payErrorCode;
        private String billingErrorMsg;
        private String payErrorMsg;

        public Payment toPayment(User user) {
            return Payment.builder()
                    .user(user)
                    .lastTransactionKey(lastTransactionKey)
                    .paymentKey(paymentKey)
                    .orderId(orderId)
                    .orderName(orderName)
                    .billingKey(billingKey)
                    .customerKey(customerKey)
                    .amount(amount)
                    .method(method)
                    .requestedAt(requestedAt)
                    .approvedAt(approvedAt)
                    .cancel(cancel)
                    .nowPayAmount(nowPayAmount)
                    .nextPayAmount(nextPayAmount)
                    .nextPay(nextPay)
                    .date(date)
                    .status(status)
                    .build();
        }

        // Refund 객체 변환
        public Refund toRefund(User user) {
            return Refund.builder()
                    .lastTransactionKey(lastTransactionKey)
                    .paymentKey(paymentKey)
                    .cancelReason(cancelReason)
                    .requestedAt(requestedAt)
                    .approvedAt(approvedAt)
                    .cancelAmount(cancelAmount)
                    .user(user)
                    .build();
        }
    }
}
