package com.uni.uni_erp.controller.payment;

import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.repository.payment.PaymentRepository;
import com.uni.uni_erp.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SsalesController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;



    //테스트메인페이지
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


    // 금고관리
    @GetMapping("/safe")
    public String getSafe(Model model) {
        Integer posNowAmount = 100000; // 예시 금액
        model.addAttribute("posNowAmount", posNowAmount);
        return "/sales/safe"; // JSP 파일 경로
    }

    @PostMapping("/safe")
    public ResponseEntity<?> createSafe(@RequestBody Map<String, Object> request) {
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

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, Object> request) {
        String amountStr = (String) request.get("amount");
        Integer amount;

        try {
            amount = Integer.valueOf(amountStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("출금 금액 형식이 잘못되었습니다."); // 잘못된 형식 처리
        }

        // 여기에서 출금 로직 구현 (예: 데이터베이스 업데이트 등)

        return ResponseEntity.ok("출금이 완료되었습니다."); // 성공 메시지
    }

    // 오픈 마감
    @GetMapping("/openAndClosed")
    public String getOpenAndClosed(Model model) {
        Payment payment = paymentService.findById(1);
        int status = payment.getStatus();
        System.out.println(status);
        model.addAttribute("status",status);
        return "/sales/openAndClosed"; // JSP 파일 경로
    }

    @PostMapping("/open")
    @ResponseBody
    public ResponseEntity<String> postClosed() {
        Payment payment = paymentService.findById(1);
        payment.setStatus(1); // 상태를 1로 변경
        paymentService.updateStatus(payment);
        return ResponseEntity.ok("Success"); // 성공 메시지 반환
    }

    @PostMapping("/close")
    @ResponseBody
    public ResponseEntity<String> postOpen() {
        Payment payment = paymentService.findById(1);
        payment.setStatus(0); // 상태를 0으로 변경
        paymentService.updateStatus(payment);
        return ResponseEntity.ok("Success"); // 성공 메시지 반환
    }





}
