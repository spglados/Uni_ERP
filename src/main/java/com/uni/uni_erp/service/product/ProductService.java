package com.uni.uni_erp.service.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.dto.product.IngredientDTO;
import com.uni.uni_erp.dto.product.ProductDTO;
import com.uni.uni_erp.repository.erp.product.IngredientRepository;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * 상품 아이디로 설정되어있는 재료 리스트 Select
     * @param productId
     * @return 각 상품에서 필요한 재료 List
     */
    public List<IngredientDTO> getIngredientByProductId(Integer productId) {
        List<Ingredient> ingredients = ingredientRepository.findByProductId(productId);
        List<IngredientDTO> dtos = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            dtos.add(ingredient.toIngredientDTO());
        }
        return dtos;
    }

    public List<ProductDTO> getProductByStoreId(Integer storeId) {
        List<ProductDTO> productList = new ArrayList<>();
        for (Product product : productRepository.findProductByStoreId(storeId)) {
            productList.add(product.toProductDTO());
        }
        return productList;
    }

}
