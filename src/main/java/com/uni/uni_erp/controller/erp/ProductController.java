package com.uni.uni_erp.controller.erp;

import com.google.gson.Gson;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.erp.product.IngredientDTO;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.dto.erp.product.ProductDTO;
import com.uni.uni_erp.exception.errors.Exception401;
import com.uni.uni_erp.service.product.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.ArrayList;

@Controller
@RequestMapping("/erp/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final HttpSession session;
    private final Gson gson;

    @GetMapping("/list")
    public String listPage() {
        return "erp/product/list";
    }

    @GetMapping({"/registration", "/registration/{productName}"})
    public String registerPage(Model model, @PathVariable(name = "productName", required = false) String productName) {
        // session 에서 스토어 아이디를 가져온 다음 해당 가게에 등록되어 있는 상품 리스트를 반환
        Integer storeId = (Integer) session.getAttribute("storeId");
        if (storeId == null) {
            throw new Exception401("관리하고 있는 가게가 없습니다.");
        }

        List<MaterialDTO> materialDTOList = productService.getMaterialList(storeId);
        List<String> materialList = new ArrayList<>();
        for (MaterialDTO materialDTO : materialDTOList) {
            materialList.add(materialDTO.getName());
        }

        if(productName != null) {
            model.addAttribute("productName", productName);
        }

        model.addAttribute("productList", productService.getProductByStoreId(storeId));
        model.addAttribute("materialDTOList", gson.toJson(materialDTOList));
        model.addAttribute("materialList", gson.toJson(materialList));
        return "erp/product/register";
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> saveProduct(@ModelAttribute ProductDTO productDTO) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        User user = (User) session.getAttribute("userSession");
        productService.saveProduct(productDTO, storeId, user.getId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/ingredient/{productId}")
    public ResponseEntity<List<IngredientDTO>> getIngredient(@PathVariable Integer productId) {
        List<IngredientDTO> dto = productService.getIngredientByProductId(productId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/ingredient/{productId}")
    public ResponseEntity<Integer> saveIngredient(@RequestBody IngredientDTO dto) {
        System.out.println(dto.toString());
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productService.saveIngredient(dto));
    }

    @PutMapping("/ingredient")
    public ResponseEntity<IngredientDTO> updateIngredient(@RequestBody IngredientDTO dto) {
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productService.updateIngredient(dto));
    }

    @DeleteMapping("/ingredient/{ingredientId}")
    public ResponseEntity<Boolean> deleteIngredient(@PathVariable(name = "ingredientId") Integer ingredientId) {
        System.out.println("ingredientId : " + ingredientId);
        if (ingredientId == null) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }
}
