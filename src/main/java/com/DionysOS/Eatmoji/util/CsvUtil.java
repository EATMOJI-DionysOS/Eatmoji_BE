package com.DionysOS.Eatmoji.util;

import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.io.InputStream;


public class CsvUtil {

    public static String findColumnValue(InputStream inputStream, int columnIndexToSearch, String valueToSearch, int columnIndexToReturn) {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                if (row.length > Math.max(columnIndexToSearch, columnIndexToReturn)
                        && row[columnIndexToSearch].trim().equalsIgnoreCase(valueToSearch)) {
                    return row[columnIndexToReturn].trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
