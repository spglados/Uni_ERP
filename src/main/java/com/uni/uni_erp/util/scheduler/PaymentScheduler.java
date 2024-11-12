package com.uni.uni_erp.util.scheduler;

import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.service.common.SubscribeDurationService;
import com.uni.uni_erp.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentService paymentService;
    private final SubscribeDurationService subscribeDurationService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void schedulePayment() {
        List<Payment> payments = paymentService.findAllWithNonZeroStatus();

        for (Payment payment : payments) {
            try {
                String nextPayDate = payment.getNextPay();
                LocalDate nextPay = LocalDate.parse(nextPayDate);
                LocalDate today = LocalDate.now();

                if (nextPay.isEqual(today)) {
                    String day = String.valueOf(nextPay.getDayOfMonth());

                    paymentService.authorizeBillingAndAutoPayment(
                            payment.getLastTransactionKey(),
                            payment.getCustomerKey(),
                            payment.getOrderId(),
                            payment.getOrderName(),
                            payment.getNextPayAmount(),
                            payment.getUser().getId(),
                            day
                    );


                    subscribeDurationService.updateEndTimeByUserId(payment.getUser().getId());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

