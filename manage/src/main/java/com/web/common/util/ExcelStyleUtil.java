package com.web.common.util;

import org.apache.poi.ss.usermodel.*;
import java.util.HashMap;
import java.util.Map;

public class ExcelStyleUtil {
    private final Workbook workbook;
    private final Map<String, CellStyle> styles = new HashMap<>();

    public ExcelStyleUtil(Workbook workbook) {
        this.workbook = workbook;
        createStyles();
    }

    private void createStyles() {
        DataFormat dataFormat = workbook.createDataFormat();

        // 제목 (굵은 글씨, 가운데 정렬, 크게)
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        styles.put("title", titleStyle);

        // 헤더 (굵은 글씨, 회색 배경, 테두리)
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        addBorder(headerStyle);
        styles.put("header", headerStyle);

        // 일반 텍스트 (왼쪽 정렬)
        CellStyle leftStyle = workbook.createCellStyle();
        leftStyle.setAlignment(HorizontalAlignment.LEFT);
        leftStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        addBorder(leftStyle);
        styles.put("text_left", leftStyle);

        // 일반 텍스트 (가운데 정렬)
        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        addBorder(centerStyle);
        styles.put("text_center", centerStyle);

        // 숫자 (오른쪽 정렬, 천 단위 콤마)
        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0"));
        addBorder(numberStyle);
        styles.put("number", numberStyle);

        // Number subtotal 조합
        CellStyle subTotaCellStyle = workbook.createCellStyle();
        subTotaCellStyle.cloneStyleFrom(styles.get("number"));
        subTotaCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subTotaCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        subTotaCellStyle.setFont(boldFont);
        addBorder(subTotaCellStyle);
        styles.put("subTotal", subTotaCellStyle);


        // 소수점 2자리 숫자
        CellStyle decimalStyle = workbook.createCellStyle();
        decimalStyle.setAlignment(HorizontalAlignment.RIGHT);
        decimalStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
        addBorder(decimalStyle);
        styles.put("decimal", decimalStyle);

        // 날짜 (yyyy-mm-dd 형식)
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setAlignment(HorizontalAlignment.CENTER);
        dateStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd"));
        addBorder(dateStyle);
        styles.put("date", dateStyle);

        // 자동 줄 바꿈 텍스트 (wrap)
        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);
        addBorder(wrapStyle);
        styles.put("wrap_text", wrapStyle);
    }

    private void addBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    public CellStyle getStyle(String name) {
        return styles.getOrDefault(name, workbook.createCellStyle());
    }

    private final Map<String, CellStyle> borderStyleCache = new HashMap<>();

    public void setRegionBorder(Sheet sheet, int startRow, int endRow, int startCol, int endCol) {
        for (int rowIdx = startRow; rowIdx <= endRow; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            if (row == null) row = sheet.createRow(rowIdx);

            for (int colIdx = startCol; colIdx <= endCol; colIdx++) {
                Cell cell = row.getCell(colIdx);
                if (cell == null) cell = row.createCell(colIdx);

                CellStyle originalStyle = cell.getCellStyle();
                String key = "bordered:" + originalStyle.hashCode();

                CellStyle cachedStyle = borderStyleCache.get(key);
                if (cachedStyle == null) {
                    cachedStyle = workbook.createCellStyle();
                    cachedStyle.cloneStyleFrom(originalStyle);
                    cachedStyle.setBorderTop(BorderStyle.THIN);
                    cachedStyle.setBorderBottom(BorderStyle.THIN);
                    cachedStyle.setBorderLeft(BorderStyle.THIN);
                    cachedStyle.setBorderRight(BorderStyle.THIN);
                    borderStyleCache.put(key, cachedStyle);
                }

                cell.setCellStyle(cachedStyle);
            }
        }
    } 
}
