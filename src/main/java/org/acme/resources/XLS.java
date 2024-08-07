package org.acme.resources;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

import jakarta.enterprise.context.ApplicationScoped;


public class XLS {

    private List<Map<String, String>> dataexcel;

    public List<Map<String, String>> GetMap() {
        return dataexcel;
    }

    public void convertToMap(InputStream inputStream) {

        try {
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            List<String> headers = new ArrayList<>();
            List<Map<String, String>> tempJson = new ArrayList<>();

            int i = 0 ;
            for (Row row : sheet) {
                int key = 0;
                Map<String, String> map = new HashMap<>();
                for (Cell cell : row) {
                    if(i == 0)
                    {
                        headers.add(cell.getStringCellValue());
                    }else{
                        switch (cell.getCellType()) {
                            case STRING:
                                map.put(headers.get(key), cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    map.put(headers.get(key), new SimpleDateFormat("yyyy-MM-dd")
                                    .format(cell.getDateCellValue()).toString());
                                } else {
                                    map.put(headers.get(key), String.valueOf(cell.getNumericCellValue()));
                                    
                                }
                                break;
                            case BOOLEAN:
                                map.put(headers.get(key), String.valueOf(cell.getBooleanCellValue()));
                                break;
                            default:
                                System.out.print("Unknown value\t");
                                break;
                        }
                    }

                    key++;
                    
                }
                if(!map.isEmpty())
                {
                    tempJson.add(map);
                }
                i++;
            }
            
            inputStream.close();
            this.dataexcel = tempJson;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
