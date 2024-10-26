package com.uni.uni_erp.util.unit;

import com.uni.uni_erp.exception.errors.Exception400;
import com.uni.uni_erp.util.Str.UnitCategory;
import org.springframework.stereotype.Component;

@Component
public class UnitCommonValidation {

    // 공통 유효성 검사 메서드 추가
    public static void validateSubAmount(double subAmount) {
        if (subAmount <= 0) {
            throw new Exception400("subAmount가 유효하지 않습니다: " + subAmount);
        }
    }

    public static void validateSubUnit(UnitCategory actualSubUnit, UnitCategory expectedSubUnit) {
        if (!actualSubUnit.equals(expectedSubUnit)) {
            throw new Exception400("subUnit이 일치하지 않습니다: 예상 " + expectedSubUnit + ", 실제 " + actualSubUnit);
        }
    }

}
