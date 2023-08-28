package org.example.service.impl;

import org.example.service.ReadService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ReadServiceImpl implements ReadService {
    @Override
    public List<String> readFromFile(File file) {
        List<String> orders;

        try {
            orders = Files.readAllLines(file.toPath());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
}
