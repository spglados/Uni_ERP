package com.uni.uni_erp.service.product;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.dto.product.IngredientDTO;
import com.uni.uni_erp.dto.product.ProductDTO;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.erp.inventory.MaterialRepository;
import com.uni.uni_erp.repository.erp.product.IngredientsRepository;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import com.uni.uni_erp.util.Str.UnitCategory;
import com.uni.uni_erp.util.sort.LanguageSortUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository productRepository;
    private final IngredientsRepository ingredientsRepository;
    private final MaterialRepository materialRepository;
    private final StoreRepository storeRepository;

    /**
     * 상품 아이디로 설정되어있는 재료 리스트 Select
     * @param productId
     * @return 각 상품에서 필요한 재료 List
     */
    public List<IngredientDTO> getIngredientByProductId(Integer productId) {
        List<Ingredient> ingredients = ingredientsRepository.findByProductId(productId);
        List<IngredientDTO> dtoList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            dtoList.add(ingredient.toIngredientDTO());
        }
        return dtoList;
    }

    public List<ProductDTO> getProductByStoreId(Integer storeId) {
        List<ProductDTO> productList = new ArrayList<>();
        for (Product product : productRepository.findProductByStoreId(storeId)) {
            productList.add(product.toProductDTO());
        }
        return productList;
    }

    public List<String> getMaterialList(Integer storeId) {
        List<String> materialList = materialRepository.findNameByStoreId(storeId);
        LanguageSortUtil.koreanSortDesc(materialList);
        return materialList;
    }

    @Transactional
    public int saveIngredient(IngredientDTO dto) {
        System.out.println("-----------------");
        System.out.println(dto.toString());
        System.out.println("-----------------");
        Product product = productRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            throw new Exception404("상품을 찾을 수 없습니다.");
        }
        return ingredientsRepository.save(Ingredient.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .unit(UnitCategory.valueOf(dto.getUnit().toUpperCase()))
                .product(product)
                .build()).getId();
    }

    @Transactional
    public IngredientDTO updateIngredient(IngredientDTO dto) {
        Ingredient ingredient = ingredientsRepository.findById(dto.getProductId()).orElse(null);
        if (ingredient == null) {
            throw new Exception404("재료를 찾을 수 없습니다.");
        }
        ingredient.setName(dto.getName());
        ingredient.setAmount(dto.getAmount());
        ingredient.setUnit(UnitCategory.valueOf(dto.getUnit().toUpperCase()));
        return ingredient.toIngredientDTO();
    }

    @Transactional
    public void deleteIngredient(Integer ingredientId) {
        ingredientsRepository.deleteById(ingredientId);
    }

    @Transactional
    public ProductDTO saveProduct(ProductDTO dto, Integer storeId, Integer userId) {
        dto.setStoreId(storeId);
        dto.setUserId(userId);

        // ProductDTO를 Product 엔티티로 변환 (id는 아직 없음)
        Product product = dto.toProduct(userId, storeId);
        product.setStore(storeRepository.findById(storeId).orElse(null));

        // 스토어가 없는 경우 처리
        if (product.getStore() == null) {
            return null;
        }

        // 먼저 Product를 저장해서 id를 생성
        productRepository.save(product);

        // 저장된 후 id가 생성된 상태에서 productCode 계산
        String productCodeStr = userId.toString() + storeId.toString() + product.getId().toString(); // id는 이제 저장 후에 사용할 수 있음
        Long productCode = Long.parseLong(productCodeStr);
        product.setProductCode(productCode);


        // productCode가 설정된 후 다시 저장
        productRepository.save(product);

        // 저장 후 DTO로 변환해서 반환
        return product.toProductDTO();
    }




}