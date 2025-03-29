package com.example.demo.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Component
public class CsvFileManager {
    
    public List<String> getCsvList(String csvRoute) {
        try (CSVReader reader = new CSVReader(new FileReader(csvRoute))) {
            return reader.readAll().stream().map(row -> row[0]).collect(Collectors.toList());
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error while reading avaliableinSV.csv file", e);
        }
    }
}
