package com.uni.uni_erp.controller;

import com.uni.uni_erp.dto.product.IngredientDTO;
import com.uni.uni_erp.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ProductService productService;

    @GetMapping("/ingredient/{productId}")
    public ResponseEntity<List<IngredientDTO>> getIngredient(@PathVariable Integer productId) {
        List<IngredientDTO> dto = productService.getIngredientByProductId(productId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

}
