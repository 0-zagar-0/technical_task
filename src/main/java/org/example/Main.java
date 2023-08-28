package org.example;

import org.example.service.ReadService;
import org.example.service.ReportService;
import org.example.service.WriteService;
import org.example.service.impl.ReadServiceImpl;
import org.example.service.impl.ReportServiceImpl;
import org.example.service.impl.WriteServiceImpl;

import java.io.File;
import java.util.List;

public class Main {
    private static final File fileInput = new File("src/main/resources/input.txt");
    private static final File fileOutput = new File("src/main/resources/output.txt");

    public static void main(String[] args) {
        ReadService orderReadService = new ReadServiceImpl();
        List<String> data = orderReadService.readFromFile(fileInput);

        ReportService reportService = new ReportServiceImpl();
        List<String> reportData = reportService.reportData(data);

        WriteService writeService = new WriteServiceImpl();
        writeService.writeToFile(reportData, fileOutput);
    }
}