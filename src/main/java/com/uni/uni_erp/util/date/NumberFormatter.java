package com.uni.uni_erp.util.date;

import java.text.DecimalFormat;

public class NumberFormatter {

    public static String formatToPrice(Integer price) {
        // DecimalFormat을 사용하여 3자리마다 콤마를 넣고, 앞에 \를 붙임
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(price);
    }

    public static String formatToPrice(String price) {
        // DecimalFormat을 사용하여 3자리마다 콤마를 넣고, 앞에 \를 붙임
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(price);
    }

    public static String formatToDouble(double v) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(v);
    }

}
