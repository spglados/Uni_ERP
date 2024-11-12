package com.uni.uni_erp.dto.erp.material;

import com.uni.uni_erp.domain.entity.erp.product.*;
import com.uni.uni_erp.util.str.UnitCategory;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class MaterialDTO {
    private Integer id;
    private String name;
    private UnitCategory unit;
    private UnitCategory subUnit;

    public MaterialDTO(String name, UnitCategory unit, UnitCategory subUnit) {
        this.name = name;
        this.unit = unit;
        this.subUnit = subUnit;
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

        private int price;

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
            this.price = materialOrder.getPrice();
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MaterialSaveDTO {

        private String name;
        private Long materialCode;
        private String category;
        private String unit;
        private Double subAmount;
        private String subUnit;
        private Double alarmCycle;
        private String alarmUnit;

        public MaterialSaveDTO(Material material) {
            this.name = material.getName();
            this.materialCode = material.getMaterialCode();
            this.category = material.getCategory();
            this.unit = material.getUnit().toString();
            this.subAmount = material.getSubAmount();
            this.subUnit = material.getSubUnit().toString();
            this.alarmCycle = material.getAlarmCycle();
            this.alarmUnit = material.getAlarmUnit().toString();
        }

        public Material toMaterial() {
            return Material.builder()
                    .name(this.name)
                    .category(this.category)
                    .unit(UnitCategory.valueOf(this.unit))
                    .subAmount(this.subAmount)
                    .subUnit(UnitCategory.valueOf(this.subUnit))
                    .alarmCycle(this.alarmCycle)
                    .alarmUnit(UnitCategory.valueOf(this.alarmUnit))
                    .build();
        }

        public MaterialStatus toMaterialStatus(Material material) {
            return MaterialStatus.builder()
                    .theoreticalAmount(0.0)
                    .actualAmount(0.0)
                    .loss(0.0)
                    .material(material)
                    .build();
        }

        public MaterialAdjustment toAdjustment(Material material) {
            return MaterialAdjustment.builder()
                    .amount(0.0)
                    .subAmount(0.0)
                    .previousLossAmount(0.0)
                    .material(material)
                    .orders(new ArrayList<>())
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MaterialDayAdjustmentDTO {
        private long materialCode;
        private double actualAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MaterialMonthAdjustmentDTO {
        private long materialCode;
        private String materialName;
        private double monthReceiveAmount;
        private double useAmount;
        private String unit;

        public MaterialMonthAdjustmentDTO(Material material) {
            this.materialCode = material.getMaterialCode();
            this.materialName = material.getName();
            this.monthReceiveAmount = 0.0;
            this.useAmount = 0.0;
            this.unit = material.getUnit().toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MaterialDisposalListDTO {
        private long materialCode;
        private String materialName;
        private String category;
        private String unit;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDisposalListDTO {
        private long productCode;
        private String productName;
        private String category;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DisposalSaveDTO {

        private List<MaterialDisposalSaveDTO> materials = new ArrayList<>();
        private List<ProductDisposalSaveDTO> products = new ArrayList<>();

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MaterialDisposalSaveDTO {
            long materialCode;
            String materialName;
            String category;
            double disposalAmount;
            LocalDate disposalDate;

            public MaterialDisposal toMaterialDisposal(Material material) {
                return MaterialDisposal.builder()
                        .materialCode(this.materialCode)
                        .amount(this.disposalAmount)
                        .disposalDate(this.disposalDate)
                        .material(material)
                        .build();
            }

        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ProductDisposalSaveDTO {
            long productCode;
            String productName;
            String category;
            double disposalAmount;
            LocalDate disposalDate;

            public ProductDisposal toProductDisposal(Product product) {
                return ProductDisposal.builder()
                        .productCode(this.productCode)
                        .amount(this.disposalAmount)
                        .disposalDate(this.disposalDate)
                        .product(product)
                        .build();
            }

        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DisposalHistoryDTO {

        private String type;
        private long code;
        private String name;
        private String category;
        private double disposalAmount;
        private String unit;
        private LocalDate disposalDate;

        public DisposalHistoryDTO(MaterialDisposal materialDisposal) {
            this.type = "자재";
            this.code = materialDisposal.getMaterialCode();
            this.name = materialDisposal.getMaterial().getName();
            this.category = materialDisposal.getMaterial().getCategory();
            this.disposalAmount = materialDisposal.getAmount();
            this.unit = materialDisposal.getMaterial().getUnit().toString();
            this.disposalDate = materialDisposal.getDisposalDate();
        }

        public DisposalHistoryDTO(ProductDisposal productDisposal) {
            this.type = "상품";
            this.code = productDisposal.getProductCode();
            this.name = productDisposal.getProduct().getName();
            this.category = productDisposal.getProduct().getCategory();
            this.disposalAmount = productDisposal.getAmount();
            this.unit = "개";
            this.disposalDate = productDisposal.getDisposalDate();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class nearingExpirationDateDTO {
        private long materialCode;
        private String materialName;
        private LocalDate expirationDate;

        public nearingExpirationDateDTO(MaterialOrder materialOrder) {
            this.materialCode = materialOrder.getMaterial().getMaterialCode();
            this.materialName = materialOrder.getMaterial().getName();
            this.expirationDate = materialOrder.getExpirationDate();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AlarmCycleMaterialDTO {
        private long materialCode;
        private String materialName;
        private double theoreticalAmount;
        private double actualAmount;
        private String unit;

        public AlarmCycleMaterialDTO(MaterialStatus materialStatus) {
            this.materialCode = materialStatus.getMaterial().getMaterialCode();
            this.materialName = materialStatus.getMaterial().getName();
            this.theoreticalAmount = materialStatus.getTheoreticalAmount();
            this.actualAmount = materialStatus.getActualAmount();
            this.unit = materialStatus.getMaterial().getUnit().toString();
        }

    }

}
