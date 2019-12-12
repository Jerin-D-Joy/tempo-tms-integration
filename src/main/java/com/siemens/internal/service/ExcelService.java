package com.siemens.internal.service;

import com.siemens.internal.utils.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ExcelService {

    @Autowired
    private ExcelReader excelReader;

    public void readFromExcel() {
        File file = new File("D:/tmp/Tempo-TMS/TMS Activity List.xlsx");
        excelReader.readFromExcel(file);
    }
}
