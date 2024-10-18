package com.uni.uni_erp.dto.erp.material;

import com.uni.uni_erp.util.Str.UnitCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
