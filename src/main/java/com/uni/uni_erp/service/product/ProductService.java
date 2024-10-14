package com.uni.uni_erp.service.product;

import com.uni.uni_erp.repository.product.IngredientRepository;
import com.uni.uni_erp.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

}
