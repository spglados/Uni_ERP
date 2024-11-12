package com.uni.uni_erp.util.unit;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.util.str.UnitCategory;
import org.springframework.stereotype.Component;

@Component
public class KgToGConverter implements UnitConverter {

    @Override
    public boolean canConvert(UnitCategory from, UnitCategory to) {
        return from == UnitCategory.KG && to == UnitCategory.G;
    }

    @Override
    public double convert(double amount, Material material) {
        // 1 KG = 1000 G
        return amount * 1000.0;
    }
}
