package com.uni.uni_erp.util.define;

public class Define_HR {
    // 범용 속성 key 값
    public static final String DATALIST = "dataList";

    // 초과 근무 시간 기준
    public static final int WEEK_OVER_MINUTES = 40 * 60;
    public static final int DAY_OVER_MINUTES = 8 * 60;
    public static final int WEEKLY_HOLIDAY_MINUTES = 15 * 60;

    // 4대 보험 비율
    public static final double NATIONAL_PENSION = 0.045;
    public static final double HEALTH_INSURANCE = 0.03545;
    public static final double EMPLOYMENT_INSURANCE = 0.009;
    public static final double EMPLOYMENT_INSURANCE_EMPLOYER = 0.0115;
    public static final double INDUSTRIAL_ACCIDENT_COMPENSATION_INSURANCE = 0.008;

    // 4대 보험 상하한
    public static final int NATIONAL_PENSION_MAX = 5_240_000;
    public static final int NATIONAL_PENSION_MIN = 330_000;
    public static final int HEALTH_INSURANCE_MAX = 104_436_000;
    public static final int HEALTH_INSURANCE_MIN = 270_000;
}
