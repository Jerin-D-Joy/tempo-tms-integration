package com.siemens.internal.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Component
public class ExcelReader {

    public void readFromExcel(File file) {
        try {
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            Row row;
            Cell cell;
            int rows = sheet.getLastRowNum();
            int cols = sheet.getRow(0).getLastCellNum();
            log.info("Rows : {} # Columns : {}", rows, cols);
            for(int i = 0; i < rows; i++) {
                row = sheet.getRow(i);
                for (int j = 0; j < cols; j++) {
                    cell = row.getCell(j);
                    log.info("# {}", cell);
                }
            }

        } catch (Exception ex) {
            log.error("Exception while reading from excel : ", ex);
        }
    }
}
