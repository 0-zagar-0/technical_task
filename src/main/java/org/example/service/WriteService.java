package org.example.service;

import java.io.File;
import java.util.List;

public interface WriteService {
    void writeToFile(List<String> data, File file);
}
