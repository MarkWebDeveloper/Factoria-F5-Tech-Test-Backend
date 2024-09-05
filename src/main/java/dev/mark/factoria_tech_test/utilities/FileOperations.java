package dev.mark.factoria_tech_test.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOperations {
    public boolean deleteIfExists(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }
}