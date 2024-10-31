package com.uni.uni_erp.controller.pos;

import com.uni.uni_erp.domain.entity.SalesRefund;
import com.uni.uni_erp.domain.entity.erp.pos.Pos;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.dto.erp.product.ProductDTO;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.dto.sales.SalesInsertDTO;
import com.uni.uni_erp.dto.sales.SalesRefundInsertDTO;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.invertory.InventoryService;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/erp/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;
    private final SalesService salesService;
    private final InventoryService inventoryService;
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
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.now().with(LocalTime.MAX);

        List<SalesDTO> previousOrders = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(todayStart, todayEnd, storeId);
        Page<Product> productListByCategory = posService.getProductsByStoreIdAndCategory(storeId, category, page - 1, size);
        model.addAttribute("productList", productListByCategory);
        model.addAttribute("totalPages", productListByCategory.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("category", category);
        model.addAttribute("previousOrders", previousOrders);

        // 유통기한 임박 자재
        List<MaterialDTO.nearingExpirationDateDTO> nearingExpirationDateList = inventoryService.nearingExpirationDate(session);

        // 재고 부족 알람 리스트
        // TODO 반드시 알람 단위가 메인 단위와 일치해야 결과가 나옴 !! 공지 필수 !!!
        List<MaterialDTO.AlarmCycleMaterialDTO> alarmCycleList = inventoryService.alarmCycle(session);
        model.addAttribute("nearingExpirationDateList", nearingExpirationDateList);
        model.addAttribute("alarmCycleList", alarmCycleList);

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
        String paymentMethod;
        double totalAmount;
        JSONArray items;
        try {
            requestBody = new String(request.getInputStream().readAllBytes());
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(requestBody);
            items = jsonObject.getJSONArray("items");
            totalAmount = jsonObject.getDouble("totalAmount");
            paymentMethod = jsonObject.getString("paymentMethod");
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

        List<ProductDTO.ProductSalesDTO> productSalesDTOList = new ArrayList<>();

        // Create SalesDetail entities
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = null;
            SalesDetailDTO salesDetailDTO;
            try {
                item = items.getJSONObject(i);
                salesDetailDTO = SalesDetailDTO.builder()
                        .itemCode(item.getLong("productCode"))
                        .itemName(item.getString("name"))
                        .quantity(item.getInt("quantity"))
                        .unitPrice(item.getInt("price"))
                        .build();
                productSalesDTOList.add(ProductDTO.ProductSalesDTO.builder().productCode(item.getLong("productCode")).quantity(item.getInt("quantity")).build());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            salesService.saveSalesDetail(salesDetailDTO, salesService.findLatestOrderNum());
        }


        boolean checkStock = inventoryService.calcMaterialByProductSales(productSalesDTOList, session);

        if(!checkStock) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("재고 부족으로 인해 계산할 수 없습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Sales inserted successfully!");
    }

    @GetMapping("/sales-detail")
    public ResponseEntity<?> getSalesDetail(@RequestParam Integer orderNum, HttpServletRequest request) {

        List<SalesDetailDTO> salesDetailDTO = salesService.findByOrderNum(Collections.singletonList(orderNum));

        return ResponseEntity.ok(salesDetailDTO);
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refund(HttpServletRequest request, HttpSession session) {
        String requestBody = null;
        int orderNum;
        String refundMethod;
        JSONArray items;
        try {
            requestBody = new String(request.getInputStream().readAllBytes());
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(requestBody);
            items = jsonObject.getJSONArray("items");
            orderNum = jsonObject.getInt("selectedOrderNum");
            refundMethod = jsonObject.getString("selectedOption");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<SalesDetailDTO> originalSalesDTO = salesService.findByOrderNum(Collections.singletonList(orderNum));
        List<Map<String, Object>> jsonList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            jsonList.add(item.toMap());
        }

        List<SalesDetailDTO> newSalesDTO = new ArrayList<>();
        for (Map<String, Object> item : jsonList) {
            SalesDetailDTO salesDetailJSON = SalesDetailDTO.builder()
                    .itemCode(Long.valueOf((Integer) item.get("productCode")))
                    .itemName((String) item.get("name"))
                    .quantity((Integer) item.get("quantity"))
                    .unitPrice((Integer) item.get("price"))
                    .build();
            newSalesDTO.add(salesDetailJSON);
        }

        List<SalesDetailDTO> salesDetail = salesService.compareQuantities(originalSalesDTO, newSalesDTO);

        List<SalesRefundInsertDTO> salesRefundDTOList = new ArrayList<>();

        if (!salesDetail.isEmpty()) {
            for (SalesDetailDTO salesDetailDTO : salesDetail) {
                SalesRefundInsertDTO salesRefundInsertDTO = SalesRefundInsertDTO.builder()
                        .itemCode(salesDetailDTO.getItemCode())
                        .itemName(salesDetailDTO.getItemName())
                        .quantity(salesDetailDTO.getQuantity())
                        .unitPrice(salesDetailDTO.getUnitPrice())
                        .refundStatus(refundMethod.equals("cancel") ? String.valueOf(SalesRefund.RefundStatus.취소) : String.valueOf(SalesRefund.RefundStatus.환불))
                        .build();

                salesRefundDTOList.add(salesRefundInsertDTO);

                salesService.saveSalesRefund(salesRefundInsertDTO, orderNum);
            }
            inventoryService.cancelOrder(salesRefundDTOList);
            return ResponseEntity.status(HttpStatus.OK).body(refundMethod.equals("cancel") ? "취소 완료" : "환불 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("err");
        }
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

    @GetMapping("/deposit")
    public String getdeposit(HttpSession session, Model model) {

        Integer storeId = (Integer) session.getAttribute("storeId");
        Pos pos = posService.getPosDetail(storeId);
        Long posNowAmount = pos.getAmount();
        Integer posNowAmountInt = posNowAmount.intValue();

        model.addAttribute("posNowAmount", posNowAmountInt);

        return "/pos/deposit"; // JSP file path
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> addAmount(HttpSession session, @RequestBody Map<String, Object> request) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        String amountStr = (String) request.get("amount");
        if (amountStr == null || amountStr.isEmpty()) {
            return ResponseEntity.badRequest().body("금액을 입력해 주세요.");
        }

        try {
            Integer tempAmount = Integer.valueOf(amountStr);
            Long amount = tempAmount.longValue();

            // 금액이 음수인 경우 처리
            if (amount < 0) {
                return ResponseEntity.badRequest().body("입금 금액은 양수여야 합니다.");
            }

            // 서비스 호출
            posService.addAmount(storeId, amount);

            // 성공 응답 반환
            return ResponseEntity.ok("입금이 완료되었습니다.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("유효한 금액을 입력해 주세요.");
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }


}
