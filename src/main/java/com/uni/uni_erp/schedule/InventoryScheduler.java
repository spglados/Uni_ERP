package com.uni.uni_erp.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryScheduler {

    @Scheduled(cron = "0 0 4 * * *")
    public void dailyMaterialStatusRegistration() {

    }

    @Scheduled(cron = "0 55 23 * * *")
    public void notRegisteredMaterialStatusRegistration() {

    }


}
