package com.uni.uni_erp.util.date;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateFormatter {

    public static String toDate(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(timestamp);
    }

}