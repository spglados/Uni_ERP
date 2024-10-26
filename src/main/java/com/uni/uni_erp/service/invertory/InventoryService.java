package com.uni.uni_erp.service.invertory;

import com.uni.uni_erp.domain.entity.erp.product.*;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.dto.erp.product.ProductDTO;
import com.uni.uni_erp.exception.errors.Exception400;
import com.uni.uni_erp.exception.errors.Exception401;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.erp.inventory.MaterialStatusRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialOrderRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialAdjustmentRepository;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import com.uni.uni_erp.util.date.NumberFormatter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final MaterialRepository materialRepository;
    private final MaterialOrderRepository materialOrderRepository;
    private final ProductRepository productRepository;
    private final MaterialAdjustmentRepository materialAdjustmentRepository;
    private final MaterialStatusRepository materialStatusRepository;
    private final UnitConversionService unitConversionService;

    @Transactional
    public List<MaterialDTO.MaterialManagementDTO> getMaterialManagementList(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        List<Product> productList = productRepository.findProductByStoreId(storeId);
        if (productList == null || productList.isEmpty()) {
            throw new Exception404("등록된 상품이 없습니다.");
        }

        List<Material> materialList = materialRepository.findAllByStoreId(storeId);
        if (materialList == null || materialList.isEmpty()) {
            throw new Exception404("자재가 존재하지 않습니다.");
        }

        List<Integer> materialIdList = materialList.stream()
                .map(Material::getId)
                .collect(Collectors.toList());

        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialId(materialIdList);
        if (materialOrderList == null || materialOrderList.isEmpty()) {
            throw new Exception404("자재 입고 내역이 없습니다.");
        }

        // Material ID를 키로 하는 MaterialOrder 리스트 맵핑
        Map<Integer, List<MaterialOrder>> ordersByMaterialId = materialOrderList.stream()
                .collect(Collectors.groupingBy(order -> order.getMaterial().getId()));

        // Product ID와 이름을 저장한 맵 생성
        Map<Integer, String> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, Product::getName));

        // Ingredient를 Material ID로 매핑
        Map<Integer, Set<Integer>> materialToProductIds = productList.stream()
                .flatMap(product -> {
                    List<Ingredient> ingredients = product.getIngredients();
                    if (ingredients != null) {
                        return ingredients.stream()
                                .map(ingredient -> new AbstractMap.SimpleEntry<>(ingredient.getMaterial().getId(), product.getId()));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toSet())
                ));

        List<MaterialDTO.MaterialManagementDTO> materialStatusList = materialList.stream()
                .map(material -> {
                    List<MaterialOrder> orders = ordersByMaterialId.getOrDefault(material.getId(), Collections.emptyList());

                    List<LocalDate> enterDates = orders.stream()
                            .map(MaterialOrder::getReceiptDate)
                            .filter(Objects::nonNull)
                            .sorted()
                            .collect(Collectors.toList());

                    List<LocalDate> expirationDates = orders.stream()
                            .filter(order -> Boolean.TRUE.equals(order.getIsUse()))
                            .map(MaterialOrder::getExpirationDate)
                            .filter(Objects::nonNull)
                            .sorted()
                            .collect(Collectors.toList());

                    LocalDate lastEnterDate = enterDates.isEmpty() ? null : enterDates.get(enterDates.size() - 1);
                    Integer stockCycle = null;
                    if (enterDates.size() > 1) {
                        Period period = Period.between(enterDates.get(enterDates.size() - 2), lastEnterDate);
                        stockCycle = period.getDays();
                    }

                    LocalDate expirationDate = expirationDates.isEmpty() ? null : expirationDates.get(0);

                    Set<Integer> usedProductIds = materialToProductIds.getOrDefault(material.getId(), Collections.emptySet());
                    Map<Integer, String> useProductList = usedProductIds.stream()
                            .collect(Collectors.toMap(id -> id, productMap::get));

                    boolean imminentDate = expirationDate != null &&
                            ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) <= 3;

                    double alarmCycle = material.getAlarmCycle() != null ? material.getAlarmCycle() : 0.0;

                    return MaterialDTO.MaterialManagementDTO.builder()
                            .materialCode(material.getMaterialCode())
                            .name(material.getName())
                            .category(material.getCategory())
                            .unit(material.getUnit() != null ? material.getUnit().toString() : "")
                            .lastEnterDate(lastEnterDate)
                            .expirationDate(expirationDate)
                            .stockCycle(stockCycle)
                            .alarmCycle(Double.toString(alarmCycle))
                            .alarmUnit(material.getAlarmUnit() != null ? material.getAlarmUnit().toString() : "")
                            .imminent(imminentDate)
                            .useProduct(useProductList)
                            .build();
                })
                .collect(Collectors.toList());

        return materialStatusList;
    }

    /**
     * 자재 입고내역 데이터 리스트
     *
     * @param session
     * @return
     */
    @Transactional
    public List<MaterialDTO.MaterialOrderDTO> getMaterialOrder(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        List<MaterialOrder> materialOrderList = materialOrderRepository.findByStoreId(storeId);
        List<MaterialDTO.MaterialOrderDTO> materialOrderDTOList = new ArrayList<>();
        if (!materialOrderList.isEmpty()) {
            for (MaterialOrder materialOrder : materialOrderList) {
                materialOrderDTOList.add(new MaterialDTO.MaterialOrderDTO(materialOrder));
            }
        }
        return materialOrderDTOList;
    }

    /**
     * 각 자재들의 상태 확인
     *
     * @param session
     * @return
     */
    public List<MaterialDTO.MaterialStatusDTO> getMaterialStatus(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }
        List<MaterialDTO.MaterialStatusDTO> materialStatusDTOList = new ArrayList<>();

        List<Product> productList = productRepository.findProductByStoreId(storeId);

        if (productList.isEmpty()) {
            throw new Exception404("등록된 상품이 없습니다.");
        }

        List<MaterialStatus> materialStatusList = materialStatusRepository.findByStoreId(storeId);

        if (materialStatusList.isEmpty()) {
            throw new Exception404("등록된 자재가 없습니다.");
        }

        Map<Integer, String> useProductList = new HashMap<>();

        for (MaterialStatus materialStatus : materialStatusList) {

            for (Product product : productList) {
                List<Ingredient> useIngredientList = product.getIngredients();

                if (useIngredientList == null || useIngredientList.isEmpty()) {
                    break;
                }

                for (Ingredient ingredient : useIngredientList) {
                    if (ingredient.getMaterial().getId().equals(materialStatus.getMaterial().getId())) {
                        useProductList.put(product.getId(), product.getName());
                    }
                }
            }

            materialStatusDTOList.add(MaterialDTO.MaterialStatusDTO.builder()
                    .materialCode(materialStatus.getMaterial().getMaterialCode())
                    .name(materialStatus.getMaterial().getName())
                    .category(materialStatus.getMaterial().getCategory())
                    .theoreticalAmount(materialStatus.getTheoreticalAmount())
                    .actualAmount(materialStatus.getActualAmount())
                    .unit(materialStatus.getMaterial().getUnit().toString())
                    .loss(materialStatus.getLoss())
                    .useProduct(useProductList)
                    .build());
        }

        return materialStatusDTOList;
    }

    /**
     * 어떤 자재가 어떤 단위를 가지고 있는 지
     *
     * @param session
     * @return
     */
    public List<MaterialDTO> getMaterialListForOrder(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        return materialRepository.findDTOByStoreId(storeId);
    }

    /**
     * 입고 내역 저장 메소드
     *
     * @param materialOrderDTO
     * @return
     */
    @Transactional
    public MaterialOrder saveMaterialOrder(HttpSession session, MaterialDTO.MaterialOrderDTO materialOrderDTO) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        MaterialOrder materialOrder = materialOrderDTO.toMaterialOrder();
        Optional<Material> material = materialRepository.findById(materialOrderDTO.getMaterialId());
        if (material.isEmpty()) {
            throw new Exception404("입고내역 저장 중에 자재 쪽에서 문제가 발생했습니다.");
        }
        materialOrder.setMaterial(material.orElse(null));
        materialOrder.setAdjustment(materialAdjustmentRepository.findByMaterialId(materialOrderDTO.getMaterialId()));

        MaterialStatus status = materialStatusRepository.findByMaterialId(materialOrderDTO.getMaterialId());
        status.setTheoreticalAmount(status.getTheoreticalAmount() + materialOrderDTO.getAmount());
        status.setActualAmount(status.getActualAmount() + materialOrderDTO.getAmount());
        status.setLoss(Double.valueOf(NumberFormatter.formatToDouble(status.getActualAmount() - status.getTheoreticalAmount())));

        return materialOrderRepository.save(materialOrder);
    }

    @Transactional
    public void calcMaterialByProductSales(List<ProductDTO.ProductSalesDTO> productSalesDTOList, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        // 제품과 자재 상태를 조회하고 맵으로 변환
        List<Product> productList = productRepository.findProductByStoreId(storeId);
        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getProductCode, Function.identity()));

        List<MaterialStatus> materialStatusList = materialStatusRepository.findByStoreId(storeId);
        Map<Integer, MaterialStatus> materialStatusMap = materialStatusList.stream()
                .collect(Collectors.toMap(ms -> ms.getMaterial().getId(), Function.identity()));

        // 판매된 각 상품에 대해 처리
        for (ProductDTO.ProductSalesDTO productSalesDTO : productSalesDTOList) {
            Product product = productMap.get(productSalesDTO.getProductCode());
            if (product == null) {
                log.warn("Product not found for code: " + productSalesDTO.getProductCode());
                throw new Exception400("판매된 상품 중 존재하지 않는 상품이 있습니다: " + productSalesDTO.getProductCode());
            }

            for (Ingredient ingredient : product.getIngredients()) {
                int materialId = ingredient.getMaterial().getId();
                MaterialStatus materialStatus = materialStatusMap.get(materialId);
                if (materialStatus == null) {
                    log.warn("MaterialStatus not found for material ID: " + materialId);
                    throw new Exception400("자재 상태를 찾을 수 없습니다: " + materialId);
                }

                if(materialStatus.getMaterial().getUnit().equals(ingredient.getUnit())) {
                    double newSameUnitTheoreticalAmount = NumberFormatter.formatToTwoDecimal(materialStatus.getTheoreticalAmount() - ingredient.getAmount());
                    materialStatus.setTheoreticalAmount(newSameUnitTheoreticalAmount);
                    continue;
                }

                double usedAmount = unitConversionService.convert(
                        ingredient.getAmount() * productSalesDTO.getQuantity(),
                        ingredient.getUnit(),
                        materialStatus.getMaterial().getUnit(),
                        materialStatus.getMaterial()
                );
                double newTheoreticalAmount = NumberFormatter.formatToTwoDecimal(materialStatus.getTheoreticalAmount() - usedAmount);
                materialStatus.setTheoreticalAmount(newTheoreticalAmount);
            }
        }

        materialStatusRepository.saveAll(materialStatusList);
    }

}
