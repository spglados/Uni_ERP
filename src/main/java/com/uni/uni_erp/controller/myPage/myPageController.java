package com.uni.uni_erp.controller.myPage;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.domain.entity.payment.Refund;
import com.uni.uni_erp.dto.store.StoreSaveDTO;
import com.uni.uni_erp.service.payment.PaymentService;
import com.uni.uni_erp.service.refund.RefundService;
import com.uni.uni_erp.service.user.StoreService;
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
    private final RefundService refundService;
    private final StoreService storeService;

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
        int payPk = Integer.valueOf(paymentId);
        String cancelReason = request.get("cancelReason");
        paymentService.updatePaymentCancelReason(payPk, cancelReason);
        return ResponseEntity.ok().body(Map.of("success", true));
    }

    @GetMapping("/refundHistory")
    public String refundHistoryPage(Model model, @SessionAttribute(value = "userSession") User principal) {
        int userPk = principal.getId();
        List<Refund> refund = refundService.getRefundById(userPk);
        model.addAttribute("refund", refund);
        return "/myPage/refundHistory";
    }

    @GetMapping("/refund")
    public String refund(Model model) {
        List<Payment> payments = paymentService.findAll();
        model.addAttribute("payments", payments);
        return "/myPage/refund";
    }

    @GetMapping("/contact")
    public String contactPage(Model model,@SessionAttribute(value = "userSession") User principal) {
        //TODO 병합후 합치기
        //List<Contact> contact = contactService.findAllById(principal.getId());
        //model.addAttribute("contact",contact);
        return "/myPage/contact";
    }

    @GetMapping("/contactDetail/{id}")
    public String contactDetailPage(Model model,@SessionAttribute(value = "userSession") User principal) {
        //Contact contact = contactService.findById(principal.getId());
        //model.addAttribute("contact",contact);
        return "/myPage/contactDetail";
    }

    @GetMapping("/storeList")
    public String storeListPage(Model model,@SessionAttribute(value = "userSession") User principal){
        List<Store> store= storeService.findAllById(principal.getId());

        Integer paymentCount = paymentService.getCountOfPaymentsWithStatusNotZero(principal.getId());
        Integer storeCount = storeService.getStoreCountByUserId(principal.getId());
        model.addAttribute("storeCount",storeCount);
        model.addAttribute("store",store);
        model.addAttribute("count",paymentCount);
        return "/myPage/storeList";
    }


    @GetMapping("/saveStore")
    public String saveStorePage() {
        return "/myPage/saveStore";
    }

    @PostMapping("/saveStore")
    public ResponseEntity<String> registerStore(@RequestBody StoreSaveDTO storeSaveDTO, @SessionAttribute(value = "userSession") User principal) {
        System.out.println(storeSaveDTO);
        storeSaveDTO.setUserId(principal.getId());

        storeService.registerStore(storeSaveDTO); // 가게 등록
        return ResponseEntity.ok("가게가 등록되었습니다!"); // 문자열 응답
    }
}

