package com.uni.uni_erp.controller.pos;

import com.uni.uni_erp.domain.entity.erp.pos.Pos;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.dto.sales.SalesInsertDTO;
import com.uni.uni_erp.service.sales.SalesService;
import com.uni.uni_erp.service.pos.PosService;
import com.uni.uni_erp.service.user.StoreService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/erp/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;
    private final SalesService salesService;
    private final StoreService storeService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 포스 메인 페이지 요청
     *
     * @return
     */
    @GetMapping("/main")
    public String showPosMainPage(@RequestParam(defaultValue = "1", name = "page") int page,
                                  @RequestParam(defaultValue = "12", name = "size") int size,
                                  @RequestParam(required = false) String category,
                                  Model model,
                                  HttpSession session) {

        Integer storeId = (Integer) session.getAttribute("storeId");
        Page<Product> productListByCategory = posService.getProductsByStoreIdAndCategory(storeId, category, page - 1, size);
        model.addAttribute("productList", productListByCategory);
        model.addAttribute("totalPages", productListByCategory.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("category", category);

        Store store = storeService.findById(storeId);
        int status24 = store.getIs24Hours();
        int status = store.getIsOpen();
        model.addAttribute("status24", status24);
        model.addAttribute("status", status);

        return "pos/posMain";  // posMain.css 화면 반환
    }


    /**
     * 가상 포스 결제 요청
     *
     * @return
     */
    @PostMapping("/payment")
    public ResponseEntity<?> posPayment(HttpServletRequest request, HttpSession session) {
        String requestBody = null;
        double totalAmount;
        JSONArray items;
        try {
            requestBody = new String(request.getInputStream().readAllBytes());
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(requestBody);
            items = jsonObject.getJSONArray("items");
            totalAmount = jsonObject.getDouble("totalAmount");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Create a new Sales entity
        SalesInsertDTO salesInsertDTO = SalesInsertDTO.builder()
                .orderNum(salesService.findLatestOrderNum() + 1)
                .totalPrice((int) totalAmount)
                .salesDate(LocalDateTime.now().withNano(0))
                .storeId((Integer) session.getAttribute("storeId")) // Replace with the actual store ID
                .build();

        // Save the Sales entity
        salesService.saveSales(salesInsertDTO);

        // Create SalesDetail entities
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = null;
            SalesDetailDTO salesDetailDTO;
            try {
                item = items.getJSONObject(i);
                salesDetailDTO = SalesDetailDTO.builder()
                        .itemCode(item.getLong("productId"))
                        .itemName(item.getString("name"))
                        .quantity(item.getInt("quantity"))
                        .unitPrice(item.getInt("price"))
                        .build();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

//            SalesRefundDTO salesRefundDTO;
//            try {
//                item = items.getJSONObject(i);
//                salesRefundDTO = SalesRefundDTO.builder()
//                        .itemCode(item.getLong("productId"))
//                        .itemName(item.getString("name"))
//                        .quantity(item.getInt("quantity"))
//                        .unitPrice(item.getInt("price"))
//                        .build();
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }

            // Save the SalesDetail entity
            salesService.saveSalesDetail(salesDetailDTO, salesService.findLatestOrderNum());
//            salesService.saveSalesRefund(salesRefundDTO, salesService.findLatestOrderNum());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Sales inserted successfully!");
    }

    // TODO 서치원
    // 시재점검
    @GetMapping("/inspection")
    public String getInspection(Model model, HttpSession session) {

        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();


        model.addAttribute("posNowAmount", posNowAmountInt);
        return "/pos/inspection"; // JSP 파일 경로
    }

    @PostMapping("/inspection")
    public ResponseEntity<?> createInspection(HttpSession session, @RequestBody Map<String, Object> request) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();

        // 입력받은 금액을 String으로 가져오고, Integer로 변환
        String amountStr = (String) request.get("totalAmount");
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
        return "/pos/safe"; // JSP 파일 경로
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
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();

        String amountStr = (String) request.get("amount");
        Long amount;

        try {
            Integer tempAmount = Integer.valueOf(amountStr); // Convert String to Integer

            if(posNowAmountInt < tempAmount) {
                return ResponseEntity.badRequest().body("출금하려는 금액이 포스잔액보다 더 많습니다.");
            }

            amount = tempAmount.longValue();
            posService.withdrawAmount(storeId,amount);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("출금 금액 형식이 잘못되었습니다."); // 잘못된 형식 처리
        }


        return ResponseEntity.ok("출금이 완료되었습니다."); // 성공 메시지
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

    @GetMapping("/close")
    public String getClose(HttpSession session, Model model) {

        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();

        model.addAttribute("posNowAmount", posNowAmountInt);

        return "/pos/openAndClosed"; // JSP file path
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
