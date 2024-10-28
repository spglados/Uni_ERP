package com.uni.uni_erp.controller.payment;

import com.uni.uni_erp.domain.entity.erp.pos.Pos;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.service.pos.PosService;
import com.uni.uni_erp.service.user.StoreService;
import jakarta.servlet.http.HttpSession;
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
    private final StoreService storeService;
    private final PosService posService;


    //테스트메인페이지
    @GetMapping("/sales")
    public String sales() {
        return "/sales/sales";
    }

    // 시재점검
    @GetMapping("/inspection")
    public String getInspection(Model model, HttpSession session) {

        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();


        model.addAttribute("posNowAmount", posNowAmountInt);
        return "/sales/inspection"; // JSP 파일 경로
    }

    @PostMapping("/inspection")
    public ResponseEntity<?> createInspection(HttpSession session, @RequestBody Map<String, Object> request) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();

        // 입력받은 금액을 String으로 가져오고, Integer로 변환
        String amountStr = (String) request.get("amount");
        Integer amount;

        try {
            amount = Integer.valueOf(amountStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("금액 형식이 잘못되었습니다."); // 잘못된 형식 처리
        }

        if (amount.equals(posNowAmountInt)) {
            return ResponseEntity.ok("금액이 일치합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("금액이 일치하지 않습니다.");
        }
    }


    // 금고관리
    @GetMapping("/safe")
    public String getSafe(HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();

        model.addAttribute("posNowAmount", posNowAmountInt);
        return "/sales/safe"; // JSP 파일 경로
    }

    @PostMapping("/safe")
    public ResponseEntity<?> createSafe(HttpSession session, @RequestBody Map<String, Object> request) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();

        // 입력받은 금액을 String으로 가져오고, Integer로 변환
        String amountStr = (String) request.get("amount");
        Integer amount;

        try {
            amount = Integer.valueOf(amountStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("금액 형식이 잘못되었습니다."); // 잘못된 형식 처리
        }

        if (amount.equals(posNowAmountInt)) {
            return ResponseEntity.ok("금액이 일치합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("금액이 일치하지 않습니다.");
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpSession session, @RequestBody Map<String, Object> request) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        String amountStr = (String) request.get("amount");
        Long amount;

        try {
            Integer tempAmount = Integer.valueOf(amountStr); // Convert String to Integer
            amount = tempAmount.longValue();
            posService.withdrawAmount(storeId,amount);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("출금 금액 형식이 잘못되었습니다."); // 잘못된 형식 처리
        }


        return ResponseEntity.ok("출금이 완료되었습니다."); // 성공 메시지
    }

    // 오픈 마감
    @GetMapping("/openAndClosed")
    public String getOpenAndClosed(HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();
        // 24시간가게
        Store store = storeService.findById(storeId);
        int status24 = store.getIs24Hours();

        int status = store.getIsOpen();


        model.addAttribute("posNowAmount", posNowAmountInt);
        model.addAttribute("status24", status24);
        model.addAttribute("status", status);



        return "/sales/openAndClosed"; // JSP file path
    }


    @PostMapping("/open")
    @ResponseBody
    public ResponseEntity<String> postClosed(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Store store = storeService.findById(storeId);
        store.setIsOpen(1); // 상태를 1로 변경
        storeService.updateStatus(store);
        System.out.println("[open]store" + store.getId() + "번 가게를 오픈으로 변경");
        return ResponseEntity.ok("Success"); // 성공 메시지 반환
    }

    @PostMapping("/close")
    @ResponseBody
    public ResponseEntity<String> postClose(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Store store = storeService.findById(storeId);

        if (store.getIs24Hours() == 1) {
            return ResponseEntity.badRequest().body("24시간 상태에서 마감되었습니다."); // Return error message
        }

        store.setIsOpen(0);
        storeService.updateStatus(store);
        System.out.println("[close]store" + store.getId() + "번 가게를 마감으로 변경");
        return ResponseEntity.ok("가게가 닫혔습니다. 마감완료"); // 성공 메시지 반환
    }





}
