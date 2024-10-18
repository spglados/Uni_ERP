package com.uni.uni_erp.util.Str;

public class EnumCommonUtil {

    // 문자열이 주어진 Enum 타입과 일치하는지 확인하는 메서드
    public static <E extends Enum<E>> boolean isValidStr(Class<E> enumClass, String value) {
        if (value == null) {
            return false;
        }
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    // 문자열을 Enum 타입으로 변환하는 메서드
    public static <E extends Enum<E>> E getEnumFromString(Class<E> enumClass, String value) {
        if (!isValidStr(enumClass, value)) {
            return null;
        }
        return Enum.valueOf(enumClass, value.toUpperCase());
    }

    // Enum 벨류를 문자열로 변환하는 메서드
    public static String getStringFromEnum(Enum<?> enumValue) {
        return enumValue.name();
    }
}
