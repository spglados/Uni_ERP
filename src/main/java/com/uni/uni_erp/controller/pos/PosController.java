package com.uni.uni_erp.controller.pos;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.service.pos.PosService;
import com.uni.uni_erp.service.product.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/erp/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;

    /**
     * 포스 메인 페이지 요청
     *
     * @return
     */
    @GetMapping("/main")
    public String showPosMainPage(@RequestParam(defaultValue = "0", name = "page") int page,
                                  @RequestParam(defaultValue = "12", name = "size") int size,
                                  Model model,
                                  HttpSession session) {

        Integer storeId = (Integer) session.getAttribute("storeId");

        Page<Product> productList = posService.getProductsByStoreId(storeId, page, size);
        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productList.getTotalPages());
        model.addAttribute("pageSize", size);

        return "pos/posMain";  // posMain.css 화면 반환
    }

    /**
     * 가상 포스 결제 요청
     *
     * @return
     */
    @PostMapping("/payment")
    public ResponseEntity<?> posPayment(Model model, HttpSession session) {


        // storeId 값을 모델에 추가하여 리다이렉트 시 전달
        Map<String, Object> response = new HashMap<>();
        // TODO 로직 실패시는 다른 return
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
