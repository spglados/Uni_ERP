package com.uni.uni_erp.controller.myPage;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.service.payment.PaymentService;
import com.uni.uni_erp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class myPageController {

    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping("")
    public String myPage(@SessionAttribute(value = "userSession") User principal, Model model) {
        Integer userPk = principal.getId();
        User user = userService.findById(userPk);
        model.addAttribute("user", user);
        return "/myPage/myPage";
    }

    @GetMapping("/paymentHistory")
    public String refundPage(Model model, @SessionAttribute(value = "userSession") User principal) {
        int userPk = principal.getId();
        List<Payment> payments = paymentService.findByUserId(userPk);
        Integer count = paymentService.getCountOfPaymentsWithStatusNotZero(userPk);

        model.addAttribute("count", count); // "payments"라는 키로 List<Payment> 추가
        model.addAttribute("payments", payments); // "payments"라는 키로 List<Payment> 추가

        return "/myPage/paymentHistory";
    }

    @PostMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@RequestBody Map<String, String> request,
                                         @SessionAttribute(value = "userSession") User principal) {
        String email = request.get("email"); // JSON에서 이메일 추출
        int userPk = principal.getId();
        userService.updateUserEmailByUserId(email, userPk); // 이메일 업데이트 서비스 호출
        return ResponseEntity.ok().build(); // 성공 응답
    }

    @PostMapping("/updatePhone")
    public ResponseEntity<?> updatePhone(@RequestBody Map<String, String> request,
                                         @SessionAttribute(value = "userSession") User principal) {
        String phone = request.get("phone"); // JSON에서 전화번호 추출
        int userPk = principal.getId();
        userService.updateUserPhoneByUserId(phone, userPk); // 전화번호 업데이트 서비스 호출
        return ResponseEntity.ok().build(); // 성공 응답
    }

    @PostMapping("/updateAddress")
    public ResponseEntity<?> updateAddress(@RequestBody Map<String, String> request,
                                           @SessionAttribute(value = "userSession") User principal) {
        String address = request.get("address");
        int userPk = principal.getId();
        userService.updateUserAddressByUserId(address, userPk);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updatePaymentDate")
    public ResponseEntity<?> updatePaymentDate(@RequestBody Map<String, String> request,
                                               @SessionAttribute(value = "userSession") User principal) {
        String newPaymentDate = request.get("paymentDate"); // 요청에서 결제일 가져오기
        int userPk = principal.getId(); // 세션에서 사용자 ID 가져오기

        // 결제일 업데이트
        userService.updateUserPaymentDateByUserId(newPaymentDate, userPk);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancelPayment")
    public ResponseEntity<?> cancelPayment(@RequestBody Map<String, String> request) {
        String paymentId = request.get("paymentId");
        String cancelReason = request.get("cancelReason");

        // 환불 요청 처리 로직 (예: DB 업데이트 등)

        // 성공적인 응답 반환
        return ResponseEntity.ok().body(Map.of("success", true));
    }



}

