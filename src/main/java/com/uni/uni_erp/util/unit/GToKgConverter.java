package com.uni.uni_erp.util.unit;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.util.Str.UnitCategory;
import org.springframework.stereotype.Component;

@Component
public class GToKgConverter implements UnitConverter {

    @Override
    public boolean canConvert(UnitCategory from, UnitCategory to) {
        return from == UnitCategory.G && to == UnitCategory.KG;
    }

    @Override
    public double convert(double amount, Material material) {
        // 1000 G = 1 KG
        return amount / 1000.0;
    }
}
