package com.uni.uni_erp.util.ExcelUtil;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Row;

public interface ExcelUtil<T> {

    public void createHeader(Row headerRow);
    public void fillRow(Row row, T data);

}
