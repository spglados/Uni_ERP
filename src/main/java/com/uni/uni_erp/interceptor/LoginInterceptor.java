package com.uni.uni_erp.interceptor;

import com.uni.uni_erp.domain.entity.user.User;
import com.uni.uni_erp.dto.admin.AdminDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 세션에서 로그인 정보를 확인
        User userSession = (User) request.getSession().getAttribute("userSession");

        if (request.getRequestURI().startsWith("/erp") && userSession == null) {

            // TODO 결제 로직 추가

            log.warn("로그인 정보 없음");
            response.sendRedirect("/user/login");
            return false;
        }

        if (request.getRequestURI().startsWith("/admin")) {
            return adminPreHandle(request, response);
        }

        log.warn("로그인 정보 있음");
        // 로그인 정보가 있으면 요청을 계속 진행
        return true;
    }

    public boolean adminPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 세션에서 로그인 정보를 확인
        AdminDTO adminSession = (AdminDTO) request.getSession().getAttribute("adminSession");

        if (request.getRequestURI().startsWith("/admin") && adminSession == null) {
            response.sendRedirect("/admin/login");
            return false;
        }

        return true;
    }
}
