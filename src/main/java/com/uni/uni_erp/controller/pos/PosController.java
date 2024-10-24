package com.uni.uni_erp.controller.pos;

import com.uni.uni_erp.controller.erp.SalesController;
import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.repository.sales.SalesDetailRepository;
import com.uni.uni_erp.repository.sales.SalesRepository;
import com.uni.uni_erp.service.SalesService;
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
    @Transactional(rollbackOn = Exception.class)
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
        Sales sales = Sales.builder()
                .storeId((Integer) session.getAttribute("storeId")) // Replace with the actual store ID
                .orderNum(salesService.findLatestOrderNum() + 1)
                .totalPrice((int) totalAmount)
                .salesDate(LocalDateTime.now().withNano(0))
                .build();

        // Save the Sales entity
        salesService.saveSales(sales);

        // Create SalesDetail entities
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = null;
            SalesDetail salesDetail;
            try {
                item = items.getJSONObject(i);
                salesDetail = SalesDetail.builder()
                        .itemCode(item.getLong("productCode"))
                        .itemName(item.getString("name"))
                        .quantity(item.getInt("quantity"))
                        .unitPrice(item.getInt("price"))
                        .sales(sales)
                        .build();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Save the SalesDetail entity
            salesService.saveSalesDetail(salesDetail);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Sales inserted successfully!");
    }
}
