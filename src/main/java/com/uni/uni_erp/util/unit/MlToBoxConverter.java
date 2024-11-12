package com.uni.uni_erp.util.unit;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.util.str.UnitCategory;
import org.springframework.stereotype.Component;

import static com.uni.uni_erp.util.unit.UnitCommonValidation.validateSubAmount;
import static com.uni.uni_erp.util.unit.UnitCommonValidation.validateSubUnit;

@Component
public class MlToBoxConverter implements UnitConverter {

    @Override
    public boolean canConvert(UnitCategory from, UnitCategory to) {
        return from == UnitCategory.ML && to == UnitCategory.BOX;
    }

    @Override
    public double convert(double amount, Material material) {
        double subAmount = material.getSubAmount();
        validateSubAmount(subAmount);
        validateSubUnit(material.getSubUnit(), UnitCategory.ML);
        return amount / subAmount;
    }
}