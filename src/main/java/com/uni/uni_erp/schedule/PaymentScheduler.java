package com.uni.uni_erp.schedule;

import com.uni.uni_erp.domain.entity.payment.Payment;
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

