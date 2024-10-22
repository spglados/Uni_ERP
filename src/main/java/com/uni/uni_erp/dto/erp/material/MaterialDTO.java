package com.uni.uni_erp.dto.erp.material;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.domain.entity.erp.product.MaterialAdjustment;
import com.uni.uni_erp.domain.entity.erp.product.MaterialOrder;
import com.uni.uni_erp.domain.entity.erp.product.MaterialStatus;
import com.uni.uni_erp.util.Str.UnitCategory;
import com.uni.uni_erp.util.date.PriceFormatter;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
public class MaterialDTO {
    private Integer id;
    private String name;
    private UnitCategory unit;
    private UnitCategory subUnit;

    public MaterialDTO(String name, UnitCategory unit) {
        this.name = name;
        this.unit = unit;
    }

    public MaterialDTO(Integer id, String name, UnitCategory unit, UnitCategory subUnit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.subUnit = subUnit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MaterialManagementDTO {

        private long materialCode;

        private String name;

        private String category;

        private String unit;

        private LocalDate lastEnterDate;

        private LocalDate expirationDate;

        private Integer stockCycle;

        private String alarmCycle;

        private String alarmUnit;

        private boolean imminent;

        private Map<Integer, String> useProduct;

    }

    /**
     * MaterialOrder를 위한 Data Transfer Object
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MaterialOrderDTO {

        private int id;

        private String name;

        private String price;

        private double amount;

        private String unit;

        private String supplier;

        private LocalDate receiptDate;

        private LocalDate expirationDate;

        private LocalDate enterDate;

        private Boolean isUse;

        private Integer materialId;

        private Integer statusId;

        public MaterialOrderDTO(MaterialOrder materialOrder) {
            this.id = materialOrder.getId();
            this.name = materialOrder.getName();
            this.price = PriceFormatter.formatToPrice(materialOrder.getPrice());
            this.amount = materialOrder.getAmount();
            this.unit = materialOrder.getUnit().toString();
            this.supplier = materialOrder.getSupplier();
            this.receiptDate = materialOrder.getReceiptDate();
            this.expirationDate = materialOrder.getExpirationDate();
            this.enterDate = materialOrder.getEnterDate();
            this.isUse = materialOrder.getIsUse();
            this.materialId = materialOrder.getMaterial().getId();
            this.statusId = materialOrder.getAdjustment().getId();
        }

        public MaterialOrder toMaterialOrder() {
            return MaterialOrder.builder()
                    .name(this.name)
                    .price(Integer.valueOf(this.price))
                    .amount(this.amount)
                    .unit(UnitCategory.valueOf(this.unit))
                    .supplier(this.supplier)
                    .receiptDate(this.receiptDate)
                    .expirationDate(this.expirationDate)
                    .isUse(this.isUse)
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MaterialStatusDTO {

        private long materialCode;

        private String name;

        private String category;

        private double theoreticalAmount;

        private double actualAmount;

        private String unit;

        private double loss;

        private Map<Integer, String> useProduct;

        public MaterialStatusDTO(MaterialStatus materialStatus, Map<Integer, String> useProduct) {
            this.materialCode = materialStatus.getMaterial().getId();
            this.name = materialStatus.getMaterial().getName();
            this.category = materialStatus.getMaterial().getCategory();
            this.theoreticalAmount = materialStatus.getTheoreticalAmount();
            this.actualAmount = materialStatus.getActualAmount();
            this.unit = materialStatus.getMaterial().getUnit().toString();
            this.loss = materialStatus.getLoss();
            this.useProduct = useProduct;
        }

    }

}
