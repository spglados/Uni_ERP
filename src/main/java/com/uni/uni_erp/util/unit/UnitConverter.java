package com.uni.uni_erp.util.unit;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.util.str.UnitCategory;

public interface UnitConverter {

    boolean canConvert(UnitCategory from, UnitCategory to);
    double convert(double amount, Material material);

}
