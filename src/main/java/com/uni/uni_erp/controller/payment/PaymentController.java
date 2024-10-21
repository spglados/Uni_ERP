package com.uni.uni_erp.controller.payment;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Refund;
import com.uni.uni_erp.dto.PaymentListDTO;
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
import java.util.*;

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


    @GetMapping("/refund")
    public String refundPage() {

        return "/payment/refund";
    }

    @PostMapping("/refund")
    public String cancelPayment(@RequestParam(value = "orderId", required = false) String orderId, Model model,
                                @RequestParam(value = "paymentKey", required = false) String paymentKey,
                                @RequestParam(value = "cancelReason", required = false) String cancelReason,
                                @RequestParam(value = "payPk", required = false) Integer payPk) {

        // 관리자 로그인 상태 체크
        //Admin admin = (Admin) session.getAttribute("admin");
        // TODO
        int adminId = 1;

        orderId = "2a8d00e4-2f78-4c1e-8ecc-8a6065cf6628";

        if (paymentKey != null && cancelReason != null) {

            if (orderId == null || orderId.isEmpty()) {
                model.addAttribute("message", "Order ID is required to cancel the payment.");
                return "redirect:/payment/fail";
            }

            try {
                // 결제 취소 실행
                String response = paymentService.cancelPayment(paymentKey, cancelReason, adminId, payPk);
                return "payment/cancel_success";
            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "redirect:/payment/fail";
            }

        } else {
            model.addAttribute("message", "필수 파라미터가 누락되었습니다.");
            return "redirect:/payment/fail";
        }

    }

    //TODO
    @GetMapping("/paymentList")
    public String paymentListPage() {

        return "payment/paymentList";
    }

    /**
     * 결제내역, 취소내역 리스트 전체 조회 및 검색 기능 (페이징 처리 포함)
     *
     * @return board/paymentList.jsp
     */
    @GetMapping("/searchPaymentList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> paymentListPage(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "searchRange", defaultValue = "", required = false) String searchRange,
            @RequestParam(name = "searchContents", defaultValue = "", required = false) String searchContents,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        List<PaymentListDTO> paymentList = null;
        List<Refund> refundList = null;
        int totalCount = 0;
        int totalPages = 0;
        Map<String, Object> response = new HashMap<>();

        try {
            if (type.equals("paymentList")) {
                if (!searchContents.isEmpty()) {
                    // 검색 조건이 있을 경우
                    paymentList = paymentService.searchPayList(searchContents, page, size);
                    totalCount = paymentService.getPayListCounts(searchContents);
                } else {
                    // 검색 조건이 없을 경우
                    System.out.println("결제 정보에 검색 조건이 없습니다.");
                    paymentList = paymentService.getAllPayList(page, size);
                    totalCount = paymentService.getPayListCounts();
                }

                totalPages = (int) Math.ceil((double) totalCount / size);

                response.put("paymentList", paymentList);

            } else if (type.equals("refundList")) {
                if (!searchContents.isEmpty() || !searchRange.isEmpty()) {
                    // 검색 조건이 있을 경우
                    refundList = paymentService.searchRefundList(searchRange, searchContents, page, size);
                    totalCount = paymentService.getSearchRefundCounts(searchRange, searchContents);
                } else {
                    // 검색 조건이 없을 경우
                    refundList = paymentService.getAllrefundList(page, size);
                    totalCount = paymentService.getRefundListCounts();
                }

                totalPages = (int) Math.ceil((double) totalCount / size);

                response.put("refundList", refundList);

            }

            response.put("totalCount", totalCount);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);
            response.put("pageSize", size);

            /*Admin admin = (Admin) session.getAttribute("admin");
            if (admin != null) {
                response.put("admin", admin);
            }*/

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }


    
}

