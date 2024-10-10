package com.uni.uni_erp.config;

import com.uni.uni_erp.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 특정 경로에 인터셉터를 등록
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/erp/**");  // /erp 경로 하위 요청을 인터셉터로 검사
    }
}