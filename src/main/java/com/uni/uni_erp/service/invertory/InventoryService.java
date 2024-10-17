package com.uni.uni_erp.service.invertory;

import com.uni.uni_erp.domain.entity.erp.product.Ingredient;
import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.domain.entity.erp.product.MaterialOrder;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.exception.errors.Exception401;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.erp.inventory.MaterialAdjustmentRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialOrderRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialStatusRepository;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final MaterialRepository materialRepository;
    private final MaterialOrderRepository materialOrderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<MaterialDTO.MaterialManagementDTO> getMaterialManagementList(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        if(storeId == null) {
            throw new Exception401("인증되지 않거나, 소유하고 있는 가게가 없습니다.");
        }

        List<Product> productList = productRepository.findProductByStoreId(storeId);

        if(productList.isEmpty()) {
            throw new Exception404("등록된 상품이 없습니다.");
        }

        List<Material> materialList = materialRepository.findAllByStoreId(storeId);
        List<Integer> materialIdList = new ArrayList<>();
        if(materialList.isEmpty()) {
            throw new Exception404("자재가 존재하지 않습니다.");
        }

        for(Material material : materialList) {
            materialIdList.add(material.getId());
        }

        List<MaterialOrder> materialOrderList = materialOrderRepository.findByMaterialId(materialIdList);

        if(materialOrderList == null || materialOrderList.isEmpty()) {
            throw new Exception404("자재 입고 내역이 없습니다.");
        }

        List<MaterialDTO.MaterialManagementDTO> materialStatusList = new ArrayList<>();

        for(Material material : materialList) {

            List<LocalDate> materialEnterDateList = new ArrayList<>();
            List<LocalDate> materialExpirationDateList = new ArrayList<>();
            LocalDate lastEnterDate = null;
            LocalDate expirationDate = null;
            Integer stockCycle = null;
            double alarmCycle = material.getAlarmCycle() != null ? material.getAlarmCycle() : 0.0;


            for(MaterialOrder materialOrder : materialOrderList) {
                if(materialOrder.getMaterial().getId().equals(material.getId())) {
                    materialEnterDateList.add(materialOrder.getReceiptDate().toLocalDate());
                    if(materialOrder.getIsUse() == Boolean.TRUE) {
                        materialExpirationDateList.add(materialOrder.getExpirationDate());
                    }
                   }
            }

            if(!materialEnterDateList.isEmpty() && materialEnterDateList.size() > 1) {
                lastEnterDate = materialEnterDateList.get(materialEnterDateList.size() - 1);
                if(lastEnterDate != null && materialOrderList.size() > 1) {
                    materialEnterDateList.sort(null);
                    Period period = materialEnterDateList.get(materialEnterDateList.size() - 2).until(materialEnterDateList.get(materialEnterDateList.size()- 1));
                    stockCycle = period.getDays();
                }
            } else if (materialEnterDateList.size() == 1) {
                lastEnterDate = materialEnterDateList.get(0);
            }
            if(!materialExpirationDateList.isEmpty()) {
                expirationDate = Collections.min(materialExpirationDateList);
            }

            Map<Integer, String> useProductList = new HashMap<>();

            for (Product product : productList) {
                List<Ingredient> useIngredientList = product.getIngredients();

                if(useIngredientList == null || useIngredientList.isEmpty()) {
                    break;
                }

                for(Ingredient ingredient : useIngredientList) {
                    if(ingredient.getMaterial().getId().equals(material.getId())) {
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

}
