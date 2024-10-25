package com.uni.uni_erp.controller.pos;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.dto.sales.SalesInsertDTO;
import com.uni.uni_erp.dto.sales.SalesRefundDTO;
import com.uni.uni_erp.service.payment.SalesService;
import com.uni.uni_erp.service.pos.PosService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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

@Controller
@RequestMapping("/erp/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;
    private final SalesService salesService;

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

}
