package com.uni.uni_erp.service.erp.invertory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.exception.errors.Exception400;
import com.uni.uni_erp.util.str.UnitCategory;
import com.uni.uni_erp.util.unit.UnitConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitConversionService {

    private final List<UnitConverter> converters;

    public double convert(double amount, UnitCategory from, UnitCategory to, Material material) {
        for (UnitConverter converter : converters) {
            if (converter.canConvert(from, to)) {
                return converter.convert(amount, material);
            }
        }
        throw new Exception400("지원되지 않는 단위 변환입니다: " + from + " to " + to);
    }
}