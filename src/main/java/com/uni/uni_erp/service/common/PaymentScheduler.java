package com.uni.uni_erp.service.common;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentScheduler {

    @Scheduled(cron = "0 0 0 1 * *")
    public void runMonthlyPayment() {

        Date toDay = new Date();




    }

}
