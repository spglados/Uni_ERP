package com.uni.uni_erp.controller.payment;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.exception.errors.Exception400;
import com.uni.uni_erp.service.payment.PaymentService;
import com.uni.uni_erp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    @Value("${payment.clientKey}")
    private String clientKey;
    @Value("${payment.secretKey}")
    private String secretKey;

    @GetMapping("")
    public String paymentPage() {

        return "/payment/payment";
    }

    // 단일,정기 결제 실패
    @GetMapping("/fail")
    public String paymentFail() {
        return "payment/fail"; // 결제 실패 페이지
    }


    // 정기 결제 성공
    @GetMapping("/success")
    public String success(@RequestParam("authKey") String authKey,
                          @RequestParam("customerKey") String customerKey,
                          @RequestParam("desiredPayDate") String desiredPayDate,
                          Model model) {
        try {
            // 주문 ID 생성
            String orderId = UUID.randomUUID().toString();

            User user = userService.findById(1); // 사용자 정보 가져오기
            int userPk = user.getId();

            User.Membership membership = user.getMembership();
            String orderName = null;
            int amount = 0;

            if(membership == User.Membership.COMMON) {
                amount = 50000;
                orderName = "첫번째 결제";
            } else {
                amount = 30000;
                orderName = "두번째 결제";
            }

            // 빌링키 발급과 자동 결제 실행
            String response = paymentService.authorizeBillingAndAutoPayment(authKey, customerKey, orderId,
                    orderName, amount, userPk, desiredPayDate); // 금액은 실제 금액으로 대체
            model.addAttribute("response", response);

            return "/payment/success";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/payment/fail";
        }
    }

    
}

