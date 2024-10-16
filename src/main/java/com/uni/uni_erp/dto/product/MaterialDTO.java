package com.uni.uni_erp.dto.product;

import com.uni.uni_erp.util.Str.UnitCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialDTO {
    private String name;
    private UnitCategory unit;

    public MaterialDTO(String name, UnitCategory unit) {
        this.name = name;
        this.unit = unit;
    }
}
