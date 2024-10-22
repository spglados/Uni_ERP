package com.uni.uni_erp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uni.uni_erp.adapter.LocalDateAdapter;
import com.uni.uni_erp.adapter.LocalDateTimeAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }
}
