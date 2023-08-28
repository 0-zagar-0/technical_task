package org.example.service;

import java.io.File;
import java.util.List;

public interface ReadService {
    List<String> readFromFile(File file);
}
