package com.uni.uni_erp.controller.pos;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.service.pos.PosService;
import com.uni.uni_erp.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;

    /**
     * 포스 메인 페이지 요청
     * @return
     */
    @GetMapping("/main/{storeId}")
    public String showPosMainPage(@PathVariable("storeId") Integer storeId, Model model) {

        List<Product> productList = posService.getProductsByStoreId(storeId);

        model.addAttribute("productList", productList);
        model.addAttribute("storeId", storeId);
        return "pos/posMain";  // posMain.css 화면 반환
    }

    /**
     * 가상 포스 결제 요청
     * @return
     */
    @PostMapping("/payment")
    public String posPayment(@RequestParam("items") List<String> items, Model model) {
        // items는 주문한 항목들의 리스트
        for (String item : items) {
            System.out.println("주문 항목: " + item);
        }

        return "redirect:/pos/main";
    }
}
