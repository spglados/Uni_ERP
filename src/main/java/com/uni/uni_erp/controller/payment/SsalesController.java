package com.uni.uni_erp.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SsalesController {

    @GetMapping("/sales")
    public String sales() {
        return "/sales/sales";
    }


    // 시재점검
    @GetMapping("/inspection")
    public String getInspection(Model model) {
        Integer posNowAmount = 100000; // 예시 금액
        model.addAttribute("posNowAmount", posNowAmount);
        return "/sales/inspection"; // JSP 파일 경로
    }

    @PostMapping("/inspection")
    public ResponseEntity<?> createInspection(@RequestBody Map<String, Object> request) {
        Integer posNowAmount = 100000; // 실제 비즈니스 로직에 따라 설정

        // 입력받은 금액을 String으로 가져오고, Integer로 변환
        String amountStr = (String) request.get("amount");
        Integer amount;

        try {
            amount = Integer.valueOf(amountStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("금액 형식이 잘못되었습니다."); // 잘못된 형식 처리
        }

        if (amount.equals(posNowAmount)) {
            return ResponseEntity.ok("금액이 일치합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("금액이 일치하지 않습니다.");
        }
    }




}
