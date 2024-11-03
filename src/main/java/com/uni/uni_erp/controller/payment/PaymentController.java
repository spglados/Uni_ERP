package com.uni.uni_erp.controller.payment;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.service.payment.PaymentService;
import com.uni.uni_erp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public String paymentPage(HttpSession session, Model model) {
        User principal = (User) session.getAttribute("userSession");
        if (principal == null) {
            return "/user/login";
        }

        Integer userPk = principal.getId(); 
        Integer count = paymentService.getCountOfNotCanceledPayments(userPk);

        model.addAttribute("count", count);
        return "/payment/payment";
    }

    // 결제 실패 페이지
    @GetMapping("/fail")
    public String paymentFail() {
        return "payment/fail";
    }

    // 정기 결제 성공 페이지
    @GetMapping("/success")
    public String success(@RequestParam("authKey") String authKey,
                          @RequestParam("customerKey") String customerKey,
                          @RequestParam("desiredPayDate") String desiredPayDate,
                          @SessionAttribute(value = "userSession") User principal,
                          Model model) {

        try {
            // 주문 ID 생성
            String orderId = UUID.randomUUID().toString();
            int userPk = principal.getId();
            User user = userService.findById(userPk); // 사용자 정보 가져오기
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

