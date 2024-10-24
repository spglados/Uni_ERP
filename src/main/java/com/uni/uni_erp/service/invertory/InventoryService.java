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

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final MaterialRepository materialRepository;
    private final MaterialOrderRepository materialOrderRepository;
    private final ProductRepository productRepository;
    private final MaterialAdjustmentRepository materialAdjustmentRepository;
    private final MaterialStatusRepository materialStatusRepository;

    /**
     * 자재 관리 품목 데이터 리스트
     *
     * @param session
     * @return
     */
    @Transactional
    public List<MaterialDTO.MaterialManagementDTO> getMaterialManagementList(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        List<Product> productList = productRepository.findProductByStoreId(storeId);

        if (productList.isEmpty()) {
            throw new Exception404("등록된 상품이 없습니다.");
        }

        List<Material> materialList = materialRepository.findAllByStoreId(storeId);
        List<Integer> materialIdList = new ArrayList<>();
        if (materialList.isEmpty()) {
            throw new Exception404("자재가 존재하지 않습니다.");
        }

        for (Material material : materialList) {
            materialIdList.add(material.getId());
        }

        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialId(materialIdList);

        if (materialOrderList == null || materialOrderList.isEmpty()) {
            throw new Exception404("자재 입고 내역이 없습니다.");
        }

        List<MaterialDTO.MaterialManagementDTO> materialStatusList = new ArrayList<>();

        for (Material material : materialList) {

            List<LocalDate> materialEnterDateList = new ArrayList<>();
            List<LocalDate> materialExpirationDateList = new ArrayList<>();
            LocalDate lastEnterDate = null;
            LocalDate expirationDate = null;
            Integer stockCycle = null;
            double alarmCycle = material.getAlarmCycle() != null ? material.getAlarmCycle() : 0.0;


            for (MaterialOrder materialOrder : materialOrderList) {
                if (materialOrder.getMaterial().getId().equals(material.getId())) {
                    materialEnterDateList.add(materialOrder.getReceiptDate());
                    if (materialOrder.getIsUse() == Boolean.TRUE) {
                        materialExpirationDateList.add(materialOrder.getExpirationDate());
                    }
                }
            }

            if (!materialEnterDateList.isEmpty() && materialEnterDateList.size() > 1) {
                lastEnterDate = materialEnterDateList.get(materialEnterDateList.size() - 1);
                if (lastEnterDate != null && materialOrderList.size() > 1) {
                    materialEnterDateList.sort(null);
                    Period period = materialEnterDateList.get(materialEnterDateList.size() - 2).until(materialEnterDateList.get(materialEnterDateList.size() - 1));
                    stockCycle = period.getDays();
                }
            } else if (materialEnterDateList.size() == 1) {
                lastEnterDate = materialEnterDateList.get(0);
            }
            if (!materialExpirationDateList.isEmpty()) {
                expirationDate = Collections.min(materialExpirationDateList);
            }

            Map<Integer, String> useProductList = new HashMap<>();

            for (Product product : productList) {
                List<Ingredient> useIngredientList = product.getIngredients();

                if (useIngredientList == null || useIngredientList.isEmpty()) {
                    break;
                }

                for (Ingredient ingredient : useIngredientList) {
                    if (ingredient.getMaterial().getId().equals(material.getId())) {
                        useProductList.put(product.getId(), product.getName());
                    }
                }
            }

            boolean imminentDate = expirationDate != null && ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) <= 3;

            MaterialDTO.MaterialManagementDTO dto = MaterialDTO.MaterialManagementDTO.builder()
                    .materialCode(material.getMaterialCode())
                    .name(material.getName())
                    .category(material.getCategory())
                    .unit(material.getUnit().toString())
                    .lastEnterDate(lastEnterDate)
                    .expirationDate(expirationDate)
                    .stockCycle(stockCycle)
                    .alarmCycle(Double.toString(alarmCycle))
                    .alarmUnit(material.getAlarmUnit().toString())
                    .imminent(imminentDate)
                    .useProduct(useProductList)
                    .build();
            materialStatusList.add(dto);
        }

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

    public void calcMaterialByProductSales(List<ProductDTO.ProductSalesDTO> productSalesDTOList, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        if (storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        List<Product> productDTOList = productRepository.findProductByStoreId(storeId);
        List<MaterialStatus> materialStatusDTOList = materialStatusRepository.findByStoreId(storeId);
        List<MaterialStatus> updateMaterialStatusDTOList = new ArrayList<>();

        for (ProductDTO.ProductSalesDTO productSalesDTO : productSalesDTOList) {
            for (Product product : productDTOList) {
                if (product.getProductCode().equals(productSalesDTO.getProductCode())) {

                    List<Ingredient> useIngredientList = product.getIngredients();

                    if (!useIngredientList.isEmpty()) {

                        for (Ingredient ingredient : useIngredientList) {

                            for (MaterialStatus materialStatus : materialStatusDTOList) {
                                if (ingredient.getMaterial().getId().equals(materialStatus.getMaterial().getId())) {


                                    if (ingredient.getUnit().equals(UnitCategory.KG)) {
                                        if (materialStatus.getMaterial().getUnit().equals(UnitCategory.KG)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - (ingredient.getAmount() * productSalesDTO.getQuantity()))));
                                        } else {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - ((ingredient.getAmount() * productSalesDTO.getQuantity()) / 1000))));
                                        }
                                    } else if (ingredient.getUnit().equals(UnitCategory.G)) {
                                        if (materialStatus.getMaterial().getUnit().equals(UnitCategory.G)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - (ingredient.getAmount() * productSalesDTO.getQuantity()))));
                                        } else if (materialStatus.getMaterial().getUnit().equals(UnitCategory.KG)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - ((ingredient.getAmount() * productSalesDTO.getQuantity()) / 1000))));
                                        } else if (materialStatus.getMaterial().getUnit().equals(UnitCategory.EA)) {
                                            materialStatus.setTheoreticalAmount(Double.parseDouble(NumberFormatter.formatToDouble(((materialStatus.getTheoreticalAmount() * materialStatus.getMaterial().getSubAmount()) - (ingredient.getAmount() * productSalesDTO.getQuantity())))) / 100);
                                        } else {
                                            System.out.println("materialStatus.getMaterial().getName() : " + materialStatus.getMaterial().getName());
                                            log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 문제발생 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            throw new Exception400("자재 등록 중 문제가 발생했습니다.");
                                        }
                                    } else if (ingredient.getUnit().equals(UnitCategory.L)) {
                                        if (materialStatus.getMaterial().getUnit().equals(UnitCategory.L)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - (ingredient.getAmount() * productSalesDTO.getQuantity()))));
                                        } else {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - ((ingredient.getAmount() * productSalesDTO.getQuantity()) / 1000))));
                                        }
                                    } else if (ingredient.getUnit().equals(UnitCategory.ML)) {

                                        if (materialStatus.getMaterial().getUnit().equals(UnitCategory.ML)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - (ingredient.getAmount() * productSalesDTO.getQuantity()))));
                                        } else if (materialStatus.getMaterial().getUnit().equals(UnitCategory.L)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - ((ingredient.getAmount() * productSalesDTO.getQuantity()) / 1000))));
                                        } else {
                                            System.out.println("materialStatus.getMaterial().getName() : " + materialStatus.getMaterial().getName());
                                            log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 문제발생 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            throw new Exception400("자재 등록 중 문제가 발생했습니다.");
                                        }

                                    } else if (ingredient.getUnit().equals(UnitCategory.BOX)) {

                                        if (materialStatus.getMaterial().getUnit().equals(UnitCategory.BOX)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(materialStatus.getTheoreticalAmount() - (ingredient.getAmount() * productSalesDTO.getQuantity()))));
                                        }

                                    } else if (ingredient.getUnit().equals(UnitCategory.EA)) {

                                        if (materialStatus.getMaterial().getUnit().equals(UnitCategory.BOX)) {
                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(((materialStatus.getTheoreticalAmount() * materialStatus.getMaterial().getSubAmount()) - (ingredient.getAmount() * productSalesDTO.getQuantity())))) / 100);
                                        } else if (materialStatus.getMaterial().getUnit().equals(UnitCategory.EA)) {

                                            materialStatus.setTheoreticalAmount(Double.valueOf(NumberFormatter.formatToDouble(((materialStatus.getTheoreticalAmount() * materialStatus.getMaterial().getSubAmount()) - (ingredient.getAmount() * productSalesDTO.getQuantity())))) / 100);

                                        }

                                    } else {
                                        System.out.println("materialStatus.getMaterial().getName() : " + materialStatus.getMaterial().getName());
                                        log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 문제발생 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                        throw new Exception400("자재 등록 중 문제가 발생했습니다.");
                                    }

                                }
                                updateMaterialStatusDTOList.add(materialStatus);
                            }

                        }

                    }
                }
            }
        }

        materialStatusRepository.saveAll(updateMaterialStatusDTOList);

    }

}
