package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Formation;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class RapportDAO {
    public ByteArrayOutputStream generateFormationReport(List<Formation> formations) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Formation Report");

        // Create styles
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Header row
        String[] headers = {"Domain", "Title", "Start Date", "End Date", "Budget", "Participants", "Month" };
        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Formation f : formations) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(f.getDomain().getLibelle());
            row.createCell(1).setCellValue(f.getTitle());
            row.createCell(2).setCellValue(f.getDate().toString());
            row.createCell(3).setCellValue(f.getEndDate().toString());
            row.createCell(4).setCellValue(f.getBudget());
            row.createCell(5).setCellValue(f.getParticipants().size());
            row.createCell(6).setCellValue(f.getDate().getMonth().toString());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out;
    }
}
