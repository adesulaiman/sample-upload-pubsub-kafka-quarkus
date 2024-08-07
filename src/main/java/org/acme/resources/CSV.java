package org.acme.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSV {

    private List<Map<String, String>> dataCsv;

    public List<Map<String, String>> GetMap(){
        return dataCsv;
    }

    public void convertToMap(InputStream inputStream){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            int i = 0;
            List<Map<String, String>> tempJson = new ArrayList<>();
            List<String> headers = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            for (CSVRecord csvRecord : csvParser) {
                
                for (int j = 0; j < csvRecord.size(); j++) {
                    if(i == 0)
                    {
                        headers.add(csvRecord.get(j));
                    }else{ 
                        map.put(headers.get(j), csvRecord.get(j));
                        
                    }
                }
                if(!map.isEmpty())
                {
                    tempJson.add(map);
                }
                i++;
            }

            this.dataCsv = tempJson;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
}
