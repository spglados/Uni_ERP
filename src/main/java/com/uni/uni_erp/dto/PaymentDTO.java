package com.uni.uni_erp.dto;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.domain.entity.payment.Refund;
import com.uni.uni_erp.domain.entity.payment.UserPay;
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
        private Integer userId; // 필요하고
        private String customerKey; // 필요하고
        private String billingKey; // 필요하고
        private Integer amount; // 필요하고
        private String orderName; // 필요하고

        // 결제 완료 후 받아오는 값
        private String lastTransactionKey;
        private String paymentKey;
        private String orderId;
        private String orderName2;
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

        private Integer cancelAmountSoon;
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
                    .orderName(orderName2)
                    .billingKey(billingKey)
                    .customerKey(customerKey)
                    .amount(amount)
                    .totalAmount(totalAmount)
                    .requestedAt(requestedAt)
                    .approvedAt(approvedAt)
                    .cancel(cancel)
                    .nowPayAmount(nowPayAmount)
                    .nextPayAmount(nextPayAmount)
                    .nextPay(nextPay)
                    .date(date)
                    .cancelAmount(cancelAmountSoon)
                    .build();
        }

        public UserPay toPayinfo(User user) {
            return UserPay.builder()
                    .nextPayDate(nextPay)
//                    .firstPayAmount()
//                    .secondPayAmount()
                    .build();
        }

        // Refund 객체 변환
        public Refund toRefund() {
            return Refund.builder()
                    .lastTransactionKey(lastTransactionKey)
                    .paymentKey(paymentKey)
                    .cancelReason(cancelReason)
                    .requestedAt(requestedAt)
                    .approvedAt(approvedAt)
                    .cancelAmount(cancelAmount)
                    .adminId(adminId)
                    .build();
        }
    }

    // PaymentHistroyDTO 및 다른 필드는 필요에 따라 추가
}
