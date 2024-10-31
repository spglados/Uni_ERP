package com.uni.uni_erp.service.invertory;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.*;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.dto.erp.product.ProductDTO;
import com.uni.uni_erp.dto.sales.SalesRefundInsertDTO;
import com.uni.uni_erp.exception.errors.Exception400;
import com.uni.uni_erp.exception.errors.Exception401;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.erp.inventory.*;
import com.uni.uni_erp.repository.erp.product.ProductDisposalRepository;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import com.uni.uni_erp.util.Str.UnitCategory;
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

    // 의존성 주입된 리포지토리 및 서비스들
    private final MaterialRepository materialRepository;
    private final MaterialOrderRepository materialOrderRepository;
    private final ProductRepository productRepository;
    private final MaterialAdjustmentRepository materialAdjustmentRepository;
    private final MaterialStatusRepository materialStatusRepository;
    private final UnitConversionService unitConversionService;
    private final StoreRepository storeRepository;
    private final MaterialDisposalRepository materialDisposalRepository;
    private final ProductDisposalRepository productDisposalRepository;

    /**
     * 재고 관리 리스트 조회 메소드
     *
     * @param session 현재 사용자 세션
     * @return 재고 관리 DTO 리스트
     */
    @Transactional
    public List<MaterialDTO.MaterialManagementDTO> getMaterialManagementList(HttpSession session) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        // 해당 storeId의 제품 리스트 조회
        List<Product> productList = productRepository.findProductByStoreId(storeId);
        if (productList == null || productList.isEmpty()) {
            throw new Exception404("등록된 상품이 없습니다.");
        }

        // 해당 storeId의 자재 리스트 조회
        List<Material> materialList = materialRepository.findAllByStoreId(storeId);
        if (materialList == null || materialList.isEmpty()) {
            throw new Exception404("자재가 존재하지 않습니다.");
        }

        // 자재 ID 리스트 추출
        List<Integer> materialIdList = materialList.stream()
                .map(Material::getId)
                .collect(Collectors.toList());

        // 자재 입고 내역 조회
        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialIdAndUseStatus(materialIdList);
        if (materialOrderList == null || materialOrderList.isEmpty()) {
            throw new Exception404("자재 입고 내역이 없습니다.");
        }

        // Material ID를 키로 하는 MaterialOrder 리스트 맵핑
        Map<Integer, List<MaterialOrder>> ordersByMaterialId = materialOrderList.stream()
                .collect(Collectors.groupingBy(order -> order.getMaterial().getId()));

        // Product ID와 이름을 저장한 맵 생성
        Map<Integer, String> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, Product::getName));

        // Ingredient를 Material ID로 매핑하여 자재별 사용 제품 ID 집합 생성
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

        // 자재 리스트를 순회하며 재고 관리 DTO 생성
        return materialList.stream()
                .map(material -> {
                    // 해당 자재의 입고 내역 조회
                    List<MaterialOrder> orders = ordersByMaterialId.getOrDefault(material.getId(), Collections.emptyList());

                    // 입고 날짜 리스트 정렬
                    List<LocalDate> enterDates = orders.stream()
                            .map(MaterialOrder::getReceiptDate)
                            .filter(Objects::nonNull)
                            .sorted()
                            .toList();

                    // 유통기한이 있는 주문의 유통기한 날짜 리스트 정렬
                    List<LocalDate> expirationDates = orders.stream()
                            .filter(order -> Boolean.TRUE.equals(order.getIsUse()))
                            .map(MaterialOrder::getExpirationDate)
                            .filter(Objects::nonNull)
                            .sorted()
                            .toList();

                    // 마지막 입고 날짜 계산
                    LocalDate lastEnterDate = enterDates.isEmpty() ? null : enterDates.get(enterDates.size() - 1);
                    Integer stockCycle = null;
                    if (enterDates.size() > 1) {
                        // 재고 사이클(이전 입고 날짜와 마지막 입고 날짜 간의 일수 차이) 계산
                        Period period = Period.between(enterDates.get(enterDates.size() - 2), lastEnterDate);
                        stockCycle = period.getDays();
                    }

                    // 첫 번째 유통기한 날짜 (가장 빠른 날짜)
                    LocalDate expirationDate = expirationDates.isEmpty() ? null : expirationDates.get(0);

                    // 해당 자재를 사용하는 제품 ID 집합 조회
                    Set<Integer> usedProductIds = materialToProductIds.getOrDefault(material.getId(), Collections.emptySet());
                    // 제품 ID를 이름으로 매핑한 맵 생성
                    Map<Integer, String> useProductList = usedProductIds.stream()
                            .collect(Collectors.toMap(id -> id, productMap::get));

                    // 유통기한 임박 여부 판단 (현재 날짜와 유통기한 사이가 3일 이하인 경우)
                    boolean imminentDate = expirationDate != null &&
                            ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) <= 3;

                    // 알람 사이클 값 설정 (없을 경우 0.0)
                    double alarmCycle = material.getAlarmCycle() != null ? material.getAlarmCycle() : 0.0;

                    // MaterialManagementDTO 빌더를 사용하여 DTO 생성
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
    }

    /**
     * 자재 입고내역 데이터 리스트 조회 메소드
     *
     * @param session 현재 사용자 세션
     * @return 자재 주문 DTO 리스트
     */
    @Transactional
    public List<MaterialDTO.MaterialOrderDTO> getMaterialOrder(HttpSession session) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        // 해당 storeId의 자재 입고 내역 조회
        List<MaterialOrder> materialOrderList = materialOrderRepository.findByStoreId(storeId);
        List<MaterialDTO.MaterialOrderDTO> materialOrderDTOList = new ArrayList<>();
        if (!materialOrderList.isEmpty()) {
            // 각 MaterialOrder를 DTO로 변환하여 리스트에 추가
            for (MaterialOrder materialOrder : materialOrderList) {
                materialOrderDTOList.add(new MaterialDTO.MaterialOrderDTO(materialOrder));
            }
        }
        return materialOrderDTOList;
    }

    /**
     * 자재 상태 리스트 조회 메소드
     *
     * @param session 현재 사용자 세션
     * @return 자재 상태 DTO 리스트
     */
    public List<MaterialDTO.MaterialStatusDTO> getMaterialStatus(HttpSession session) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);
        List<MaterialDTO.MaterialStatusDTO> materialStatusDTOList = new ArrayList<>();

        // 해당 storeId의 제품 리스트 조회
        List<Product> productList = productRepository.findProductByStoreId(storeId);

        if (productList.isEmpty()) {
            throw new Exception404("등록된 상품이 없습니다.");
        }

        // 해당 storeId의 자재 상태 리스트 조회
        List<MaterialStatus> materialStatusList = materialStatusRepository.findByStoreIdAndStatusDate(storeId, LocalDate.now());

        if (materialStatusList.isEmpty()) {
            // 오늘 날짜 status를 등록
            List<MaterialStatus> previousStatusList = materialStatusRepository.findByStoreId(storeId);
            for (MaterialStatus materialStatus : previousStatusList) {
                materialStatusList.add(MaterialStatus.builder()
                        .theoreticalAmount(materialStatus.getTheoreticalAmount())
                        .actualAmount(materialStatus.getActualAmount())
                        .statusDate(LocalDate.now())
                        .loss(materialStatus.getLoss())
                        .useTotalAmount(0.0)
                        .material(materialStatus.getMaterial())
                        .build());
            }
            materialStatusRepository.saveAll(materialStatusList);
        }

        // 자재 상태 리스트를 순회하며 DTO 생성
        for (MaterialStatus materialStatus : materialStatusList) {

            Map<Integer, String> useProductList = new HashMap<>();

            // 각 제품을 순회하며 해당 자재를 사용하는지 확인
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

            // MaterialStatusDTO 빌더를 사용하여 DTO 생성
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
     * 자재 리스트 조회 메소드 (주문을 위한)
     *
     * @param session 현재 사용자 세션
     * @return 자재 DTO 리스트
     */
    public List<MaterialDTO> getMaterialListForOrder(HttpSession session) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        // 해당 storeId의 자재 DTO 리스트 조회
        return materialRepository.findDTOByStoreId(storeId);
    }

    /**
     * 자재 입고 내역 저장 메소드
     *
     * @param session          현재 사용자 세션
     * @param materialOrderDTO 자재 주문 DTO
     * @return 저장된 MaterialOrder 엔티티
     */
    @Transactional
    public MaterialOrder saveMaterialOrder(HttpSession session, MaterialDTO.MaterialOrderDTO materialOrderDTO) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        checkStock(materialOrderDTO.getAmount(), "입고량이 0 이하 일 수 없습니다.");

        // DTO를 MaterialOrder 엔티티로 변환
        MaterialOrder materialOrder = materialOrderDTO.toMaterialOrder();
        // 자재 존재 여부 확인
        Optional<Material> material = materialRepository.findByIdAndStoreId(materialOrderDTO.getMaterialId(), storeId);
        if (material.isEmpty()) {
            throw new Exception404("입고내역 저장 중에 자재 쪽에서 문제가 발생했습니다.");
        }
        // 자재 설정
        materialOrder.setMaterial(material.orElse(null));
        // 자재 조정 정보 설정
        materialOrder.setAdjustment(materialAdjustmentRepository.findByMaterialId(materialOrderDTO.getMaterialId()));

        // 자재 상태 조회
        MaterialStatus status = materialStatusRepository.findByMaterialId(materialOrderDTO.getMaterialId());
        // 이론량 및 실제량 증가
        status.setTheoreticalAmount(status.getTheoreticalAmount() + materialOrderDTO.getAmount());
        status.setActualAmount(status.getActualAmount() + materialOrderDTO.getAmount());
        // 손실량 계산
        status.setLoss(Double.valueOf(NumberFormatter.formatToDouble(status.getActualAmount() - status.getTheoreticalAmount())));

        // MaterialOrder 저장
        return materialOrderRepository.save(materialOrder);
    }

    /**
     * 제품 판매에 따른 자재 사용량 계산 메소드
     *
     * @param productSalesDTOList 판매된 제품 정보 리스트
     * @param session             현재 사용자 세션
     * @return 처리 성공 여부 (재고 부족 시 false 반환)
     */
    @Transactional
    public boolean calcMaterialByProductSales(List<ProductDTO.ProductSalesDTO> productSalesDTOList, HttpSession session) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        // 해당 storeId의 제품 리스트 조회
        List<Product> productList = productRepository.findProductByStoreId(storeId);
        // 제품 코드를 키로 하는 맵 생성
        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getProductCode, Function.identity()));

        // 판매된 제품에서 필요한 자재 ID 추출
        Set<Integer> requiredMaterialIds = productSalesDTOList.stream()
                .flatMap(salesDTO -> {
                    Product product = productMap.get(salesDTO.getProductCode());
                    if (product != null) {
                        return product.getIngredients().stream()
                                .map(ingredient -> ingredient.getMaterial().getId());
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toSet());

        // 필요한 자재 ID 리스트 생성
        List<Integer> materialIdList = new ArrayList<>(requiredMaterialIds);

        // 해당 storeId의 자재 상태 리스트 조회
        List<MaterialStatus> materialStatusList = materialStatusRepository.findByStoreId(storeId);
        // 자재 ID를 키로 하는 MaterialStatus 맵 생성
        Map<Integer, MaterialStatus> materialStatusMap = materialStatusList.stream()
                .collect(Collectors.toMap(ms -> ms.getMaterial().getId(), Function.identity()));

        // 필요한 자재의 MaterialOrder를 한 번에 조회
        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialIdAndUseStatus(materialIdList);
        // MaterialCode를 키로 하는 MaterialOrder 리스트 맵 생성 (유통기한 순으로 정렬된 상태)
        Map<Long, List<MaterialOrder>> ordersByMaterialCode = materialOrderList.stream()
                .collect(Collectors.groupingBy(
                        mo -> mo.getMaterial().getMaterialCode(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(MaterialOrder::getExpirationDate))
                                        .collect(Collectors.toList())
                        )
                ));

        // 판매된 각 상품에 대해 처리
        for (ProductDTO.ProductSalesDTO productSalesDTO : productSalesDTOList) {
            Product product = productMap.get(productSalesDTO.getProductCode());
            if (product == null) {
                log.warn("Product not found for code: " + productSalesDTO.getProductCode());
                throw new Exception400("판매된 상품 중 존재하지 않는 상품이 있습니다: " + productSalesDTO.getProductCode());
            }

            int quantity = productSalesDTO.getQuantity(); // 판매 수량

            // 각 제품의 자재 사용량 계산
            for (Ingredient ingredient : product.getIngredients()) {
                int materialId = ingredient.getMaterial().getId();
                MaterialStatus materialStatus = materialStatusMap.get(materialId);
                if (materialStatus == null) {
                    log.warn("MaterialStatus not found for material ID: " + materialId);
                    throw new Exception400("자재 상태를 찾을 수 없습니다: " + materialId);
                }

                // 단위 변환 후 사용량 계산
                double usedAmount = unitConversionService.convert(
                        ingredient.getAmount() * quantity,
                        ingredient.getUnit(),
                        materialStatus.getMaterial().getUnit(),
                        materialStatus.getMaterial()
                );

                // MaterialOrder에서 유통기한 순으로 사용량 차감
                long materialCode = ingredient.getMaterial().getMaterialCode();
                useAmountByMaterialOrderForExpirationDate(ordersByMaterialCode, materialCode, usedAmount);

                // 이론량 및 실제량 감소
                double newTheoreticalAmount = NumberFormatter.formatToTwoDecimal(materialStatus.getTheoreticalAmount() - usedAmount);
                double newActualAmount = NumberFormatter.formatToTwoDecimal(materialStatus.getActualAmount() - usedAmount);

                // 음수 여부 확인
                if (newTheoreticalAmount < 0 || newActualAmount < 0) {
                    // 음수인 경우 false 반환
                    return false;
                }

                // 손실량 계산
                materialStatus.setTheoreticalAmount(newTheoreticalAmount);
                materialStatus.setActualAmount(newActualAmount);
                materialStatus.setUseTotalAmount(materialStatus.getUseTotalAmount() != null ? materialStatus.getUseTotalAmount() + usedAmount : usedAmount);
                materialStatus.setLoss(NumberFormatter.formatToTwoDecimal(newActualAmount - newTheoreticalAmount));
            }
        }

        // 변경된 자재 상태 저장
        materialStatusRepository.saveAll(materialStatusList);
        // 변경된 MaterialOrder는 영속성 컨텍스트에 의해 자동으로 저장됩니다.
        return true;
    }

    /**
     * 자재 등록 저장 메소드
     *
     * @param materialSaveDTO 자재 저장 DTO
     * @param session         현재 사용자 세션
     * @return 저장된 MaterialSaveDTO
     */
    @Transactional
    public MaterialDTO.MaterialSaveDTO saveMaterial(MaterialDTO.MaterialSaveDTO materialSaveDTO, HttpSession session) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        checkStock(materialSaveDTO.getSubAmount(), "세부 단위 양을 0이하로 정할 수 없습니다.");

        // 세션에서 사용자 정보 추출
        User user = (User) session.getAttribute("userSession");
        if (user == null) {
            throw new Exception401("로그인이 필요합니다.");
        }

        // DTO를 Material 엔티티로 변환
        Material reqMaterial = materialSaveDTO.toMaterial();

        // 해당 storeId의 Store 엔티티 설정
        reqMaterial.setStore(storeRepository.findById(storeId).get());

        // Material 저장
        Material resMaterial = materialRepository.save(reqMaterial);

        // MaterialCode 생성 (User ID + Store ID + Material ID)
        String materialCodeStr = user.getId() + "" + storeId + "" + resMaterial.getId();
        Long materialCode = Long.parseLong(materialCodeStr);

        // MaterialCode 설정 및 재저장
        resMaterial.setMaterialCode(materialCode);
        materialRepository.save(reqMaterial);

        // MaterialStatus 및 MaterialAdjustment 저장
        materialStatusRepository.save(materialSaveDTO.toMaterialStatus(resMaterial));
        materialAdjustmentRepository.save(materialSaveDTO.toAdjustment(resMaterial));

        log.debug("Material saved with code: " + materialCode);
        return new MaterialDTO.MaterialSaveDTO(resMaterial);
    }

    /**
     * 자재 일일 조정 저장 메소드
     *
     * @param session    현재 사용자 세션
     * @param reqDtoList 자재 일일 조정 DTO 리스트
     */
    @Transactional
    public boolean saveDayAdjustmentList(HttpSession session, List<MaterialDTO.MaterialDayAdjustmentDTO> reqDtoList) {
        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        // 해당 storeId의 자재 상태 리스트 조회
        List<MaterialStatus> materialStatusList = materialStatusRepository.findByStoreId(storeId);

        // 일일 조정 요청을 Map으로 변환 (MaterialCode -> ActualAmount)
        Map<Long, Double> actualAmountMap = reqDtoList.stream().collect(Collectors.toMap(
                MaterialDTO.MaterialDayAdjustmentDTO::getMaterialCode,
                MaterialDTO.MaterialDayAdjustmentDTO::getActualAmount
        ));

        boolean isUpdated = false;

        // 각 MaterialStatus에 대해 실제량과 손실량 업데이트
        for (MaterialStatus materialStatus : materialStatusList) {
            // 요청된 ActualAmount가 있는 경우 업데이트
            Double newActualAmount = actualAmountMap.get(materialStatus.getMaterial().getMaterialCode());
            if (newActualAmount != null && !Objects.equals(newActualAmount, materialStatus.getActualAmount())) {
                // 변경이 발생한 경우 isUpdated를 true로 설정
                isUpdated = true;
                materialStatus.setActualAmount(newActualAmount);
                materialStatus.setLoss(NumberFormatter.formatToTwoDecimal(newActualAmount - materialStatus.getTheoreticalAmount()));
            }
        }

        // 변경된 MaterialStatus가 있는 경우 저장
        if (isUpdated) {
            materialStatusRepository.saveAll(materialStatusList);
        }

        return isUpdated;
    }


    /**
     * 자재 폐기 목록 조회 메소드
     *
     * @param session 현재 사용자 세션
     * @return 자재 폐기 목록 DTO 리스트
     */
    public List<MaterialDTO.MaterialDisposalListDTO> getMaterialDisposalList(HttpSession session) {

        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        // 해당 storeId의 자재 리스트 조회
        List<Material> materialList = materialRepository.findAllByStoreId(storeId);

        List<MaterialDTO.MaterialDisposalListDTO> disposalList = new ArrayList<>();

        // 각 자재를 순회하며 DisposalList DTO 생성
        for (Material material : materialList) {
            disposalList.add(MaterialDTO.MaterialDisposalListDTO.builder()
                    .materialCode(material.getMaterialCode())
                    .materialName(material.getName())
                    .category(material.getCategory())
                    .unit(material.getUnit().toString())
                    .build());
        }
        return disposalList;
    }

    /**
     * 제품 폐기 목록 조회 메소드
     *
     * @param session 현재 사용자 세션
     * @return 제품 폐기 목록 DTO 리스트
     */
    public List<MaterialDTO.ProductDisposalListDTO> getProductDisposalList(HttpSession session) {

        // 세션에서 storeId 추출
        Integer storeId = getStoreId(session);

        // 해당 storeId의 제품 리스트 조회
        List<Product> productList = productRepository.findProductByStoreId(storeId);
        List<MaterialDTO.ProductDisposalListDTO> disposalList = new ArrayList<>();

        // 각 제품을 순회하며 DisposalList DTO 생성
        for (Product product : productList) {
            disposalList.add(MaterialDTO.ProductDisposalListDTO.builder()
                    .productCode(product.getProductCode())
                    .productName(product.getName())
                    .category(product.getCategory())
                    .build());
        }
        return disposalList;
    }

    /**
     * 자재 및 제품 폐기 내역 저장 메소드
     *
     * @param session    현재 사용자 세션
     * @param reqDtoList 자재 폐기 요청 DTO 리스트
     * @return 처리 성공 여부 (재고 부족 시 false 반환)
     */
    @Transactional
    public void saveDisposal(HttpSession session, MaterialDTO.DisposalSaveDTO reqDtoList) {

        Integer storeId = getStoreId(session);

        // 자재 폐기 요청 DTO 리스트 추출
        List<MaterialDTO.DisposalSaveDTO.MaterialDisposalSaveDTO> mDisposalList = reqDtoList.getMaterials();
        // 자재 폐기 요청을 Map으로 변환 (MaterialCode -> DisposalAmount)
        Map<Long, Double> mDisposalMap = mDisposalList.stream()
                .collect(Collectors.toMap(
                        MaterialDTO.DisposalSaveDTO.MaterialDisposalSaveDTO::getMaterialCode,
                        MaterialDTO.DisposalSaveDTO.MaterialDisposalSaveDTO::getDisposalAmount));

        // 필요한 자재 코드 리스트 생성
        List<Long> materialCodeList = new ArrayList<>(mDisposalMap.keySet());

        // 자재 코드로 해당 자재 호출하여 Map 생성 (MaterialCode -> Material)
        List<Material> materialList = materialRepository.findByMaterialCodes(materialCodeList);
        Map<Long, Material> materialMap = materialList.stream()
                .collect(Collectors.toMap(Material::getMaterialCode, Function.identity()));

        // 필요한 자재 ID 리스트 생성
        List<Integer> materialIdList = materialList.stream()
                .map(Material::getId)
                .collect(Collectors.toList());

        // 필요한 자재의 MaterialOrder를 한 번에 조회
        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialIdAndUseStatus(materialIdList);
        // MaterialCode를 키로 하는 MaterialOrder 리스트 맵 생성 (유통기한 순으로 정렬된 상태)
        Map<Long, List<MaterialOrder>> ordersByMaterialCode = materialOrderList.stream()
                .collect(Collectors.groupingBy(
                        mo -> mo.getMaterial().getMaterialCode(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(MaterialOrder::getExpirationDate))
                                        .collect(Collectors.toList())
                        )
                ));

        // 자재 폐기 DTO를 MaterialDisposal 엔티티로 변환 및 리스트에 추가
        List<MaterialDisposal> materialDisposalList = new ArrayList<>();
        for (MaterialDTO.DisposalSaveDTO.MaterialDisposalSaveDTO dto : mDisposalList) {
            Material material = materialMap.get(dto.getMaterialCode());
            if (material == null) {
                throw new Exception404("자재를 찾을 수 없습니다: " + dto.getMaterialCode());
            }
            materialDisposalList.add(dto.toMaterialDisposal(material));

            // MaterialOrder에서 유통기한 순으로 사용량 차감
            useAmountByMaterialOrderForExpirationDate(ordersByMaterialCode, dto.getMaterialCode(), dto.getDisposalAmount());
        }

        // 자재 상태 조회 (오늘 날짜 기준)
        List<MaterialStatus> todayStatus = materialStatusRepository.findByMaterial(materialList, LocalDate.now());
        List<MaterialStatus> allStatus = materialStatusRepository.findAllByStoreIdAndToday(storeId, LocalDate.now());

        if (todayStatus == null || todayStatus.isEmpty()) {
            log.warn("오늘 날짜의 재고 현황 데이터가 존재하지 않습니다.");
        }

        // MaterialCode를 키로 하는 MaterialStatus 맵 생성
        Map<Long, MaterialStatus> disposalStatusMap = todayStatus.stream()
                .filter(ms -> ms.getMaterial() != null && ms.getMaterial().getMaterialCode() != null)
                .collect(Collectors.toMap(
                        ms -> ms.getMaterial().getMaterialCode(),
                        Function.identity()
                ));

        Map<Long, MaterialStatus> allStatusMap = allStatus.stream()
                .filter(ms -> ms.getMaterial() != null && ms.getMaterial().getMaterialCode() != null)
                .collect(Collectors.toMap(
                        ms -> ms.getMaterial().getMaterialCode(),
                        Function.identity()
                ));

        // 자재 폐기 양만큼 이론량과 실제량 감소, 손실량 계산
        for (MaterialStatus materialStatus : todayStatus) {
            // 폐기 내역에 해당 자재가 없는 경우 건너뜀
            if (!mDisposalMap.containsKey(materialStatus.getMaterial().getMaterialCode())) {
                continue;
            }

            double disposalAmount = mDisposalMap.get(materialStatus.getMaterial().getMaterialCode());

            // 이론량 감소
            double newTheoreticalAmount = NumberFormatter.formatToTwoDecimal(
                    materialStatus.getTheoreticalAmount() - disposalAmount
            );
            // 실제량 감소
            double newActualAmount = NumberFormatter.formatToTwoDecimal(
                    materialStatus.getActualAmount() - disposalAmount
            );

            materialStatus.setTheoreticalAmount(newTheoreticalAmount);
            materialStatus.setActualAmount(newActualAmount);
            materialStatus.setUseTotalAmount(materialStatus.getUseTotalAmount() != null ? materialStatus.getUseTotalAmount() + disposalAmount : disposalAmount);

            // 손실량 계산
            materialStatus.setLoss(
                    NumberFormatter.formatToTwoDecimal(
                            materialStatus.getActualAmount() - materialStatus.getTheoreticalAmount()
                    )
            );
        }

        // 변경된 자재 상태 저장
        materialStatusRepository.saveAll(todayStatus);
        // 자재 폐기 내역 저장
        materialDisposalRepository.saveAll(materialDisposalList);

        // 제품 폐기 요청 DTO 리스트 추출
        List<MaterialDTO.DisposalSaveDTO.ProductDisposalSaveDTO> pDisposalList = reqDtoList.getProducts();

        // 제품 코드로 해당 제품 호출하여 Map 생성 (ProductCode -> Product)
        List<Product> productList =
                productRepository.findAllByStoreIdForProductCode(
                        storeId,
                        pDisposalList.stream()
                                .map(MaterialDTO.DisposalSaveDTO.ProductDisposalSaveDTO::getProductCode)
                                .collect(Collectors.toList()));

        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getProductCode, Function.identity()));

        // 필요한 자재 ID 추출
        Set<Integer> requiredMaterialIds = productList.stream()
                .flatMap(product -> product.getIngredients().stream())
                .map(ingredient -> ingredient.getMaterial().getId())
                .collect(Collectors.toSet());

        // 필요한 자재의 MaterialOrder를 한 번에 조회
        List<MaterialOrder> productMaterialOrders = materialOrderRepository.findByMaterialIdAndUseStatus(new ArrayList<>(requiredMaterialIds));
        // MaterialCode를 키로 하는 MaterialOrder 리스트 맵 생성
        Map<Long, List<MaterialOrder>> productOrdersByMaterialCode = productMaterialOrders.stream()
                .collect(Collectors.groupingBy(
                        mo -> mo.getMaterial().getMaterialCode(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(MaterialOrder::getExpirationDate))
                                        .collect(Collectors.toList())
                        )
                ));

        // 제품 폐기 DTO를 ProductDisposal 엔티티로 변환 및 리스트에 추가
        List<ProductDisposal> productDisposalList = new ArrayList<>();
        for (MaterialDTO.DisposalSaveDTO.ProductDisposalSaveDTO dto : pDisposalList) {
            Product product = productMap.get(dto.getProductCode());
            if (product == null) {
                throw new Exception404("상품을 찾을 수 없습니다: " + dto.getProductCode());
            }
            productDisposalList.add(dto.toProductDisposal(product));

            // 각 제품의 자재 사용량 계산 및 자재 상태 업데이트
            for (Ingredient ingredient : product.getIngredients()) {
                long materialCode = ingredient.getMaterial().getMaterialCode();
                MaterialStatus materialStatus = allStatusMap.get(materialCode);
                if (materialStatus == null) {
                    log.warn("MaterialStatus not found for material ID: " + materialCode);
                    throw new Exception400("자재 상태를 찾을 수 없습니다: " + materialCode);
                }

                // 단위 변환 후 사용량 계산
                double usedAmount = unitConversionService.convert(
                        ingredient.getAmount() * dto.getDisposalAmount(),
                        ingredient.getUnit(),
                        materialStatus.getMaterial().getUnit(),
                        materialStatus.getMaterial()
                );

                // MaterialOrder에서 유통기한 순으로 사용량 차감
                useAmountByMaterialOrderForExpirationDate(productOrdersByMaterialCode, materialCode, usedAmount);

                // 이론량 및 실제량 감소
                double newTheoreticalAmount = NumberFormatter.formatToTwoDecimal(
                        materialStatus.getTheoreticalAmount() - usedAmount
                );
                double newActualAmount = NumberFormatter.formatToTwoDecimal(
                        materialStatus.getActualAmount() - usedAmount
                );

                materialStatus.setTheoreticalAmount(newTheoreticalAmount);
                materialStatus.setActualAmount(newActualAmount);
                materialStatus.setUseTotalAmount(materialStatus.getUseTotalAmount() != null ? materialStatus.getUseTotalAmount() + usedAmount : usedAmount);

                // 손실량 계산
                materialStatus.setLoss(NumberFormatter.formatToTwoDecimal(newActualAmount - newTheoreticalAmount));
            }
        }

        // 변경된 자재 상태 저장
        materialStatusRepository.saveAll(todayStatus);
        // 제품 폐기 내역 저장
        productDisposalRepository.saveAll(productDisposalList);
        // 변경된 MaterialOrder는 영속성 컨텍스트에 의해 자동으로 저장됩니다.

    }


    private static Integer getStoreId(HttpSession session) {
        // 세션에서 storeId 추출
        Integer storeId = (Integer) session.getAttribute("storeId");
        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }
        return storeId;
    }

    public List<MaterialDTO.DisposalHistoryDTO> getDisposalHistory(HttpSession session) {
        Integer storeId = getStoreId(session);

        List<MaterialDisposal> materialDisposalList = materialDisposalRepository.findByStoreId(storeId);
        List<ProductDisposal> productDisposalList = productDisposalRepository.findByStoreId(storeId);

        return Stream.concat(
                        materialDisposalList.stream().map(MaterialDTO.DisposalHistoryDTO::new),
                        productDisposalList.stream().map(MaterialDTO.DisposalHistoryDTO::new)
                )
                .sorted(Comparator.comparing(MaterialDTO.DisposalHistoryDTO::getDisposalDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 특정 자재 코드로 자재 데이터 조회
     *
     * @param materialCode 자재 코드
     * @return MaterialSaveDTO 또는 null
     */
    @Transactional(readOnly = true)
    public MaterialDTO.MaterialSaveDTO getMaterialByCode(Long materialCode) {
        Optional<Material> optionalMaterial = materialRepository.findByMaterialCode(materialCode);
        if (optionalMaterial.isPresent()) {
            Material material = optionalMaterial.get();
            return new MaterialDTO.MaterialSaveDTO(material);
        }
        return null;
    }

    /**
     * 특정 자재 코드로 자재 데이터 수정
     *
     * @param materialSaveDTO 수정된 자재 데이터 DTO
     * @return 수정 성공 여부
     */
    @Transactional
    public boolean updateMaterial(MaterialDTO.MaterialSaveDTO materialSaveDTO) {

        checkStock(materialSaveDTO.getSubAmount(), "0 이하로 재고량을 수정할 수 없습니다.");


        Optional<Material> optionalMaterial = materialRepository.findByMaterialCode(materialSaveDTO.getMaterialCode());
        if (!optionalMaterial.isPresent()) {
            return false;
        }

        Material material = optionalMaterial.get();

        try {
            // 수정 가능한 필드 업데이트
            material.setName(materialSaveDTO.getName());
            material.setCategory(materialSaveDTO.getCategory());
            material.setUnit(UnitCategory.valueOf(materialSaveDTO.getUnit()));
            material.setSubAmount(materialSaveDTO.getSubAmount());
            material.setSubUnit(UnitCategory.valueOf(materialSaveDTO.getSubUnit()));
            material.setAlarmCycle(materialSaveDTO.getAlarmCycle());
            material.setAlarmUnit(UnitCategory.valueOf(materialSaveDTO.getAlarmUnit()));

            // 변경 사항 저장
            materialRepository.save(material);
            return true;
        } catch (IllegalArgumentException e) {
            // Enum 값이 잘못된 경우
            log.error("Enum 변환 오류: {}", e.getMessage());
            return false;
        }
    }

    private void checkStock(Double Amount, String msg) {
        if (Amount != null) {
            if (Amount < 0) {
                throw new Exception400(msg);
            }
        }
    }

    /**
     * 사용량에 따른 주문 내역에서 유통기한을 확인 및 상태값 변경에 필요한 메소드
     * 유통기한 순으로 정렬된 리스트 들어가 있어야함.
     *
     * @param orderList
     * @param materialCode
     * @param amount
     */
    private void useAmountByMaterialOrderForExpirationDate(Map<Long, List<MaterialOrder>> orderList, long materialCode, double amount) {
        List<MaterialOrder> materialOrderList = orderList.get(materialCode);
        if (materialOrderList == null || materialOrderList.isEmpty()) {
            // 처리할 주문이 없을 경우, 새로운 MaterialOrder를 생성하거나 예외 처리를 할 수 있습니다.
            // 하지만 요구사항에 따라 여기서는 마지막 주문에 음수 사용량을 기록합니다.
            return;
        }
        double calcAmount = amount;
        int lastIndex = materialOrderList.size() - 1;
        for (int i = 0; i <= lastIndex; i++) {
            MaterialOrder order = materialOrderList.get(i);
            double remainingUseAmount = order.getUseAmount() - calcAmount;
            if (remainingUseAmount > 0) {
                // 사용량을 차감하고 루프 종료
                order.setUseAmount(remainingUseAmount);
                break;
            } else if (remainingUseAmount == 0) {
                // 사용량이 정확히 0이 되었으므로, 사용 가능 여부를 false로 설정하고 루프 종료
                order.setUseAmount(0.0);
                order.setIsUse(false);
                break;
            } else {
                // 사용량이 음수가 되었으므로, 현재 주문의 사용량을 0으로 설정하고 사용 가능 여부를 false로 변경
                order.setUseAmount(0.0);
                order.setIsUse(false);
                calcAmount = -remainingUseAmount; // 남은 사용량을 다음 주문에서 차감하기 위해 양수로 변환
                // 마지막 주문인지 확인
                if (i == lastIndex) {
                    // 더 이상 주문이 없으므로, 마지막 주문에 음수 사용량을 기록
                    order.setUseAmount(-calcAmount);
                    // `isUse`는 이미 false로 설정되어 있음
                    break;
                }
            }
        }
    }

    public List<MaterialDTO.nearingExpirationDateDTO> nearingExpirationDate(HttpSession session) {
        Integer storeId = getStoreId(session);

        List<Material> materialList = materialRepository.findByStoreId(storeId);

        List<Integer> materialIdList = materialList.stream()
                .map(Material::getId)
                .toList();

        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialIdAndUseStatusAndStoreId(materialIdList, storeId);

        // 1. 오늘 날짜와 3일 후 날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate threeDaysLater = today.plusDays(3);

        // 2. materialOrderList에서 유통기한이 오늘부터 3일 후까지인 자재 필터링 및 DTO로 매핑
        List<MaterialDTO.nearingExpirationDateDTO> nearingExpirationList = materialOrderList.stream()
                .filter(order -> {
                    LocalDate expirationDate = order.getExpirationDate();
                    return !expirationDate.isBefore(today) && !expirationDate.isAfter(threeDaysLater);
                })
                .map(MaterialDTO.nearingExpirationDateDTO::new)
                .toList();

        return nearingExpirationList;
    }

    public List<MaterialDTO.AlarmCycleMaterialDTO> alarmCycle(HttpSession session) {
        Integer storeId = getStoreId(session);
        List<MaterialStatus> statusList = materialStatusRepository.findAlarmCycleMaterialDTOByStoreId(storeId);
        List<MaterialDTO.AlarmCycleMaterialDTO> alarmCycleDTOList = new ArrayList<>();
        for (MaterialStatus materialStatus : statusList) {
            alarmCycleDTOList.add(new MaterialDTO.AlarmCycleMaterialDTO(materialStatus));
        }
        return alarmCycleDTOList;
    }

    public List<MaterialDTO.MaterialMonthAdjustmentDTO> getMonthAdjustment(HttpSession session) {
        Integer storeId = getStoreId(session);

        List<MaterialDTO.MaterialMonthAdjustmentDTO> monthAdjustmentDTOList = new ArrayList<>();

        // 현재 연도와 월을 가져옵니다.
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        // Repository 메소드 호출
        List<MaterialOrder> orders = materialOrderRepository.findByEnterDateInCurrentMonthAndStoreId(currentYear, currentMonth, storeId);
        List<Material> materialList = materialRepository.findByStoreId(storeId);
        List<MaterialStatus> statusList = materialStatusRepository.findByCurrentMonth(currentMonth);

        for (Material m : materialList) {
            monthAdjustmentDTOList.add(new MaterialDTO.MaterialMonthAdjustmentDTO(m));
        }

        Map<Long, MaterialDTO.MaterialMonthAdjustmentDTO> monthAdjustmentMap = monthAdjustmentDTOList.stream().collect(Collectors.toMap(MaterialDTO.MaterialMonthAdjustmentDTO::getMaterialCode, Function.identity()));

        for (MaterialOrder order : orders) {
            MaterialDTO.MaterialMonthAdjustmentDTO dto = monthAdjustmentMap.get(order.getMaterial().getMaterialCode());
            dto.setMonthReceiveAmount(NumberFormatter.formatToTwoDecimal(dto.getMonthReceiveAmount() + order.getUseAmount()));
        }


        for(MaterialStatus materialStatus : statusList) {
            MaterialDTO.MaterialMonthAdjustmentDTO dto = monthAdjustmentMap.get(materialStatus.getMaterial().getMaterialCode());
            dto.setUseAmount(NumberFormatter.formatToTwoDecimal(dto.getUseAmount() + materialStatus.getUseTotalAmount()));
        }


        return monthAdjustmentDTOList;
    }

    @Transactional
    public void cancelOrder(List<SalesRefundInsertDTO> salesRefundDTOList) {
        // 환불된 상품의 제품 코드 리스트 추출
        List<Long> productCodes = salesRefundDTOList.stream()
                .map(SalesRefundInsertDTO::getItemCode)
                .collect(Collectors.toList());

        // 제품 코드로 해당 제품들을 조회
        List<Product> productList = productRepository.findAllByProductCodes(productCodes);

        // 제품 코드를 키로 하는 Product 맵 생성
        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getProductCode, Function.identity()));

        // 필요한 자재 ID 추출
        Set<Integer> requiredMaterialIds = productList.stream()
                .flatMap(product -> product.getIngredients().stream())
                .map(ingredient -> ingredient.getMaterial().getId())
                .collect(Collectors.toSet());

        // 필요한 자재 ID 리스트 생성
        List<Integer> materialIdList = new ArrayList<>(requiredMaterialIds);

        // 자재 상태 조회
        List<MaterialStatus> materialStatusList = materialStatusRepository.findByMaterialIds(materialIdList);
        // 자재 ID를 키로 하는 MaterialStatus 맵 생성
        Map<Integer, MaterialStatus> materialStatusMap = materialStatusList.stream()
                .collect(Collectors.toMap(ms -> ms.getMaterial().getId(), Function.identity()));

        // 필요한 자재의 MaterialOrder를 조회
        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialIds(materialIdList);
        // MaterialCode를 키로 하는 MaterialOrder 리스트 맵 생성 (유통기한 순으로 정렬된 상태)
        Map<Long, List<MaterialOrder>> ordersByMaterialCode = materialOrderList.stream()
                .collect(Collectors.groupingBy(
                        mo -> mo.getMaterial().getMaterialCode(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(MaterialOrder::getExpirationDate))
                                        .collect(Collectors.toList())
                        )
                ));

        // 환불된 각 상품에 대해 처리
        for (SalesRefundInsertDTO refundDTO : salesRefundDTOList) {
            Long productCode = refundDTO.getItemCode();
            Integer quantity = refundDTO.getQuantity();

            Product product = productMap.get(productCode);
            if (product == null) {
                log.warn("Product not found for code: " + productCode);
                throw new Exception400("환불된 상품 중 존재하지 않는 상품이 있습니다: " + productCode);
            }

            // 각 제품의 자재 사용량 계산 및 자재 상태 업데이트
            for (Ingredient ingredient : product.getIngredients()) {
                Material material = ingredient.getMaterial();
                Integer materialId = material.getId();
                MaterialStatus materialStatus = materialStatusMap.get(materialId);
                if (materialStatus == null) {
                    log.warn("MaterialStatus not found for material ID: " + materialId);
                    throw new Exception400("자재 상태를 찾을 수 없습니다: " + materialId);
                }

                // 단위 변환 후 사용량 계산
                double usedAmount = unitConversionService.convert(
                        ingredient.getAmount() * quantity,
                        ingredient.getUnit(),
                        material.getUnit(),
                        material
                );

                // MaterialOrder에서 유통기한 역순으로 사용량 추가
                addAmountToMaterialOrder(ordersByMaterialCode, material.getMaterialCode(), usedAmount);

                // 이론량 및 실제량 증가
                double newTheoreticalAmount = NumberFormatter.formatToTwoDecimal(
                        materialStatus.getTheoreticalAmount() + usedAmount
                );
                double newActualAmount = NumberFormatter.formatToTwoDecimal(
                        materialStatus.getActualAmount() + usedAmount
                );

                materialStatus.setTheoreticalAmount(newTheoreticalAmount);
                materialStatus.setActualAmount(newActualAmount);
                materialStatus.setUseTotalAmount(materialStatus.getUseTotalAmount() != null ? materialStatus.getUseTotalAmount() + usedAmount : usedAmount);
                // 손실량 계산
                materialStatus.setLoss(
                        NumberFormatter.formatToTwoDecimal(
                                newActualAmount - newTheoreticalAmount
                        )
                );
            }
        }

        // 변경된 자재 상태 저장
        materialStatusRepository.saveAll(materialStatusList);
        // 변경된 MaterialOrder는 영속성 컨텍스트에 의해 자동으로 저장됩니다.
    }

    private void addAmountToMaterialOrder(Map<Long, List<MaterialOrder>> ordersByMaterialCode, long materialCode, double amount) {
        List<MaterialOrder> materialOrderList = ordersByMaterialCode.get(materialCode);
        if (materialOrderList == null || materialOrderList.isEmpty()) {
            // 처리할 주문이 없을 경우
            return;
        }
        double remainingAmount = amount;
        // 유통기한 역순으로 처리 (최근에 사용된 주문부터)
        for (int i = materialOrderList.size() - 1; i >= 0; i--) {
            MaterialOrder order = materialOrderList.get(i);
            double maxUseAmount = order.getAmount();
            double currentUseAmount = order.getUseAmount();
            double availableToAdd = maxUseAmount - currentUseAmount;

            if (availableToAdd >= remainingAmount) {
                // 남은 양을 모두 현재 주문에 추가할 수 있는 경우
                order.setUseAmount(currentUseAmount + remainingAmount);
                order.setIsUse(true); // 사용 가능 상태로 변경
                break;
            } else {
                // 현재 주문에 추가할 수 있는 만큼 추가하고 다음 주문으로 이동
                order.setUseAmount(maxUseAmount);
                order.setIsUse(true);
                remainingAmount -= availableToAdd;
            }
        }
    }


}
