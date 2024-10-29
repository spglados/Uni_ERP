package com.uni.uni_erp.controller.pos;

import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.domain.entity.SalesRefund;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.dto.erp.product.ProductDTO;
import com.uni.uni_erp.dto.erp.product.ProductDTO;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.dto.sales.SalesInsertDTO;
import com.uni.uni_erp.dto.sales.SalesRefundDTO;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.invertory.InventoryService;
import com.uni.uni_erp.service.pos.PosService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.eclipse.tags.shaded.org.apache.bcel.generic.IFLT;
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
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/erp/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;
    private final SalesService salesService;
    private final InventoryService inventoryService;

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

        List<SalesDTO> previousOrders = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(todayStart, todayEnd, storeId);
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

        List<SalesDetail> salesDetailList = new ArrayList<>();
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

        salesService.saveSalesDetailList(salesDetailList);

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
        System.err.println(salesDetail);

        if (!salesDetail.isEmpty()) {
            for (SalesDetailDTO salesDetailDTO : salesDetail) {
                SalesRefundDTO salesRefundDTO = SalesRefundDTO.builder()
                        .itemCode(salesDetailDTO.getItemCode())
                        .itemName(salesDetailDTO.getItemName())
                        .quantity(salesDetailDTO.getQuantity())
                        .unitPrice(salesDetailDTO.getUnitPrice())
                        .refundStatus(refundMethod.equals("cancel") ? String.valueOf(SalesRefund.RefundStatus.취소) : String.valueOf(SalesRefund.RefundStatus.환불))
                        .build();
                salesService.saveSalesRefund(salesRefundDTO, orderNum);
            }
            return ResponseEntity.status(HttpStatus.OK).body(refundMethod.equals("cancel") ? "취소 완료" : "환불 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("err");
        }
    }

}
