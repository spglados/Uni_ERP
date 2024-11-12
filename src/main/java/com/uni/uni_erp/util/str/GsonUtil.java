package com.uni.uni_erp.util.str;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uni.uni_erp.adapter.LocalDateAdapter;
import com.uni.uni_erp.adapter.LocalDateTimeAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GsonUtil {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private GsonUtil() {
        // 인스턴스화 방지
    }

    public static <T> String convertToJson(T data) {
        return gson.toJson(data);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}