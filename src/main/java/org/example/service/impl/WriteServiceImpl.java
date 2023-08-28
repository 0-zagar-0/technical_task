package org.example.service.impl;

import org.example.service.WriteService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteServiceImpl implements WriteService {

    @Override
    public void writeToFile(List<String> data, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String dat : data) {
                writer.write(dat);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
