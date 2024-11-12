package com.uni.uni_erp.util.unit;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.util.str.UnitCategory;
import org.springframework.stereotype.Component;

@Component
public class LToMlConverter implements UnitConverter {

    @Override
    public boolean canConvert(UnitCategory from, UnitCategory to) {
        return from == UnitCategory.L && to == UnitCategory.ML;
    }

    @Override
    public double convert(double amount, Material material) {
        // 1 L = 1000 ML
        return amount * 1000.0;
    }
}