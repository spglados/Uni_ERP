package com.uni.uni_erp.dto.erp.material;

import com.uni.uni_erp.util.Str.UnitCategory;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
public class MaterialDTO {
    private String name;
    private UnitCategory unit;

    public MaterialDTO(String name, UnitCategory unit) {
        this.name = name;
        this.unit = unit;
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
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MaterialStatusDTO {

        private int id;

        private String name;

        private String category;

        private double theoreticalAmount;

        private double actualAmount;

        private String unit;

        private double loss;

        private LocalDate expirationDate;

        private LocalDate receiptDate;

        private Map<Integer, String> useProduct;

    }

}
