package com.uni.uni_erp.util.excelUtil;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelUtil<T> {

    public void createHeader(Row headerRow);
    public void fillRow(Row row, T data);

}
