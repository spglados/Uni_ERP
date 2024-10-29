package com.uni.uni_erp.service.product;

import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.dto.erp.product.IngredientDTO;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.dto.erp.product.ProductDTO;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesQuantityDTO;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.erp.inventory.MaterialRepository;
import com.uni.uni_erp.repository.erp.product.IngredientsRepository;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import com.uni.uni_erp.repository.sales.SalesRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository productRepository;
    private final IngredientsRepository ingredientsRepository;
    private final MaterialRepository materialRepository;
    private final StoreRepository storeRepository;
    private final SalesRepository salesRepository;

    /**
     * 상품 아이디로 설정되어있는 재료 리스트 Select
     *
     * @param productId
     * @return 각 상품에서 필요한 재료 List
     */
    public List<IngredientDTO> getIngredientByProductId(Integer productId) {
        List<Ingredient> ingredients = ingredientsRepository.findByProductId(productId);
        List<IngredientDTO> dtoList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            dtoList.add(new IngredientDTO(ingredient));
        }
        return dtoList;
    }

    @Transactional
    public List<ProductDTO> getProductByStoreId(Integer storeId) {
        List<ProductDTO> productList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 2024년 전체의 판매 데이터를 조회하기 위한 기간 설정
        LocalDateTime startDate = LocalDateTime.of(today.getYear(), 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(today.getYear(), 12, 31, 23, 59, 59);

        // 판매 내역 조회
        List<SalesQuantityDTO> salesQuantityList = salesRepository.findSalesQuantity(startDate, endDate, storeId);

        // 상품 목록 처리
        for (Product product : productRepository.findProductByStoreId(storeId)) {
            ProductDTO productDTO = new ProductDTO(product);

            // 판매량 초기화
            productDTO.setYearSales(0);
            productDTO.setTodaySales(0);
            productDTO.setYesterdaySales(0);
            productDTO.setMonthSales(0);
            productDTO.setPreviousMonthSales(0);

            if (!salesQuantityList.isEmpty()) {
                // 전일 날짜 계산
                LocalDate yesterday = today.minusDays(1);

                for (SalesQuantityDTO sales : salesQuantityList) {
                    // 금년도 판매량 취합
                    if (Long.valueOf(productDTO.getProductCode()).equals(sales.getItemCode())) {
                        productDTO.setYearSales(productDTO.getYearSales() + sales.getQuantity());

                        // 금일 판매량
                        if (sales.getSalesDate().toLocalDate().equals(today)) {
                            productDTO.setTodaySales(productDTO.getTodaySales() + sales.getQuantity());
                        }

                        // 전일 판매량
                        if (sales.getSalesDate().toLocalDate().equals(yesterday)) {
                            productDTO.setYesterdaySales(productDTO.getYesterdaySales() + sales.getQuantity());
                        }

                        // 금월 판매량
                        if (sales.getSalesDate().getMonthValue() == today.getMonthValue()) {
                            productDTO.setMonthSales(productDTO.getMonthSales() + sales.getQuantity());
                        }

                        // 전월 판매량
                        LocalDate previousMonth = today.minusMonths(1);
                        if (sales.getSalesDate().getMonthValue() == previousMonth.getMonthValue()) {
                            productDTO.setPreviousMonthSales(productDTO.getPreviousMonthSales() + sales.getQuantity());
                        }
                    }
                }

                // 금월 일 평균 판매량 구하기 (0으로 나누지 않도록 처리)
                int daysInMonth = today.lengthOfMonth();
                int monthSales = productDTO.getMonthSales();
                productDTO.setMonthSales(monthSales > 0 ? monthSales / daysInMonth : 0);

                // 전월 일 평균 판매량 구하기
                LocalDate previousMonth = today.minusMonths(1);
                int daysInPreviousMonth = previousMonth.lengthOfMonth();
                int previousMonthSales = productDTO.getPreviousMonthSales();
                productDTO.setPreviousMonthSales(previousMonthSales > 0 ? previousMonthSales / daysInPreviousMonth : 0);

                // 금년 일 평균 판매량 구하기
                int dayOfYear = today.getDayOfYear();
                int yearSales = productDTO.getYearSales();
                productDTO.setYearSales(yearSales > 0 ? yearSales / dayOfYear : 0);
            }
            productList.add(productDTO);
        }

        return productList;
    }

    public List<MaterialDTO> getMaterialList(Integer storeId) {
        List<MaterialDTO> materialList = materialRepository.findNameByStoreId(storeId);
        return materialList;
    }

    @Transactional
    public int saveIngredient(IngredientDTO dto) {
        Product product = productRepository.findById(dto.getProductId()).orElse(null);
        Material material = materialRepository.findByName(dto.getName());
        if (product == null) {
            throw new Exception404("상품을 찾을 수 없습니다.");
        }
        return ingredientsRepository.save(Ingredient.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .unit(UnitCategory.valueOf(dto.getUnit().toUpperCase()))
                .product(product)
                .material(material)
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
        return new IngredientDTO(ingredient);
    }

    @Transactional
    public void deleteIngredient(Integer ingredientId) {
        ingredientsRepository.deleteById(ingredientId);
    }

    @Transactional
    public ProductDTO saveProduct(ProductDTO dto, Integer storeId, Integer userId) {
        dto.setStoreId(storeId);
        dto.setUserId(userId);

        // TODO 이름 유효성 검사 추가

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
        return new ProductDTO(product);
    }

    /**
     * 특정 상품 코드로 상품 데이터 조회하여 ProductResponseDTO 반환
     *
     * @param productCode 조회할 상품 코드
     * @param storeId     조회할 스토어 ID
     * @return ProductResponseDTO 또는 null
     */
    @Transactional(readOnly = true)
    public ProductDTO.ProductResponseDTO getProductResponseDTOByProductCode(Long productCode, Integer storeId) {
        Optional<Product> optionalProduct = productRepository.findByProductCode(productCode);
        System.out.println(optionalProduct.toString());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // 스토어 ID 일치 확인
            if (!product.getStore().getId().equals(storeId)) {
                return null;
            }

            // Blob 이미지를 Base64 문자열로 변환
            String imageBase64 = null;
            if (product.getImage() != null) {
                try {
                    byte[] imageBytes = product.getImage().getBytes(1, (int) product.getImage().length());
                    imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                } catch (Exception e) {
                    log.warn("이미지 변환 실패: " + e.getMessage());
                }
            }

            // 기존 ProductDTO에서 필요한 필드 추출
            ProductDTO dto = new ProductDTO(product);
            ProductDTO.ProductResponseDTO responseDTO = ProductDTO.ProductResponseDTO.builder()
                    .id(dto.getId())
                    .productCode(dto.getProductCode())
                    .name(dto.getName())
                    .category(dto.getCategory())
                    .price(dto.getPrice())
                    .description(dto.getDescription())
                    .todaySales(dto.getTodaySales())
                    .yesterdaySales(dto.getYesterdaySales())
                    .monthSales(dto.getMonthSales())
                    .previousMonthSales(dto.getPreviousMonthSales())
                    .yearSales(dto.getYearSales())
                    .image(imageBase64)
                    .build();

            return responseDTO;
        } else {
            return null;
        }
    }

    /**
     * 특정 상품 코드로 상품 데이터 수정
     *
     * @param productCode 수정할 상품 코드
     * @param dto         수정된 상품 데이터
     * @param imageFile   수정된 이미지 파일 (선택 사항)
     * @param storeId     스토어 ID
     * @param userId      사용자 ID
     * @return 수정 성공 여부
     */
    @Transactional
    public boolean updateProductByProductCode(Long productCode, ProductDTO dto, MultipartFile imageFile, Integer storeId, Integer userId) {
        Optional<Product> optionalProduct = productRepository.findByProductCode(productCode);
        if (!optionalProduct.isPresent()) {
            return false;
        }

        Product product = optionalProduct.get();

        // 스토어 ID 일치 확인
        if (!product.getStore().getId().equals(storeId)) {
            return false;
        }

        // 수정 가능한 필드 업데이트
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        // 이미지 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Blob blob = new SerialBlob(imageFile.getBytes());
                product.setImage(blob);
            } catch (IOException e) {
                log.warn("이미지 업로드 실패: " + e.getMessage());
                return false;
            } catch (SerialException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // 변경 사항 저장
        productRepository.save(product);
        return true;
    }

    public List<String> getDistinctCategories() {
        return productRepository.findDistinctCategories();
    }

}
