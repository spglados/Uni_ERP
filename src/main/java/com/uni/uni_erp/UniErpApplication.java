package com.uni.uni_erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//스케줄러 사용을 위한 어노테이션
@EnableScheduling
public class UniErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniErpApplication.class, args);
    }

}
