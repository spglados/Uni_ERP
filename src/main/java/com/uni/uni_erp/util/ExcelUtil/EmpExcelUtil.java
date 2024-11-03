package com.uni.uni_erp.util.ExcelUtil;

import com.uni.uni_erp.dto.erp.hr.EmployeeExcelDTO;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

@Component
public class EmpExcelUtil implements ExcelUtil<EmployeeExcelDTO> {
    @Override
    public void createHeader(Row headerRow) {

        Workbook workbook = headerRow.getSheet().getWorkbook();

        // 헤더 스타일 생성 및 설정
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 각 셀에 헤더 스타일 적용
        String[] headers = {"사원번호", "이름", "직책", "생년월일", "성별", "이메일", "전화번호", "주소", "은행", "계좌번호", "재직상태",
                "근로 계약서", "건강 증명서", "건강 증명서 발급일", "신분증 사본", "은행 계좌 사본", "주민등록증"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }


    }


    @Override
    public void fillRow(Row row, EmployeeExcelDTO employeeExcelDTO) {
        Workbook workbook = row.getSheet().getWorkbook();

        // 셀 스타일 설정
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd"));

        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);

        // 데이터 입력 및 스타일 적용
        row.createCell(0).setCellValue(employeeExcelDTO.getUniqueEmployeeNumber());  // 사원번호
        row.createCell(1).setCellValue(employeeExcelDTO.getName());                   // 이름
        row.createCell(2).setCellValue(employeeExcelDTO.getEmpPositionName());        // 직책

        Cell birthdayCell = row.createCell(3);  // 생년월일
        birthdayCell.setCellValue(employeeExcelDTO.getBirthday());
        birthdayCell.setCellStyle(dateStyle);

        row.createCell(4).setCellValue(employeeExcelDTO.getGender() != null
                ? (employeeExcelDTO.getGender().name().equals("F") ? "여자" : "남자")
                : "");
        row.createCell(5).setCellValue(employeeExcelDTO.getEmail());                // 이메일
        row.createCell(6).setCellValue(employeeExcelDTO.getPhone());                // 전화번호
        row.createCell(7).setCellValue(employeeExcelDTO.getAddress());              // 주소
        row.createCell(8).setCellValue(employeeExcelDTO.getBankName());             // 은행
        row.createCell(9).setCellValue(employeeExcelDTO.getAccountNumber());        // 계좌번호
        row.createCell(10).setCellValue(
                "ACTIVE".equals(employeeExcelDTO.getEmploymentStatus()) ? "재직" :
                        "INACTIVE".equals(employeeExcelDTO.getEmploymentStatus()) ? "퇴사" :
                                "ONLEAVE".equals(employeeExcelDTO.getEmploymentStatus()) ? "휴직" : ""
        );   // 재직상태

        row.createCell(11).setCellValue(employeeExcelDTO.isEmploymentContract() ? "제출됨" : "미제출");  // 근로 계약서
        row.createCell(12).setCellValue(employeeExcelDTO.isHealthCertificate() ? "제출됨" : "미제출");   // 건강 증명서

        Cell healthCertDateCell = row.createCell(13);  // 건강 증명서 발급일
        healthCertDateCell.setCellValue(employeeExcelDTO.getHealthCertificateDate());
        healthCertDateCell.setCellStyle(dateStyle);

        row.createCell(14).setCellValue(employeeExcelDTO.isIdentificationCopy() ? "제출됨" : "미제출");  // 신분증 사본
        row.createCell(15).setCellValue(employeeExcelDTO.isBankAccountCopy() ? "제출됨" : "미제출");     // 은행 계좌 사본
        row.createCell(16).setCellValue(employeeExcelDTO.isResidentRegistration() ? "제출됨" : "미제출"); // 주민등록증
    }
}
