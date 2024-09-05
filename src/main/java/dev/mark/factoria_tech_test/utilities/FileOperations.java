package dev.mark.factoria_tech_test.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

import javax.management.RuntimeErrorException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import dev.mark.factoria_tech_test.config.StorageProperties;
import dev.mark.factoria_tech_test.images.IStorageService;
import dev.mark.factoria_tech_test.images.StorageException;
import dev.mark.factoria_tech_test.images.StorageFileNotFoundException;

@Component
public class FileOperations implements IStorageService {

    private final Path rootLocation;
    private Time time;

    public FileOperations(Time time, StorageProperties properties) {
        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.time = time;
    }

    @Override
    public void init(){
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public Path resolvePath(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void saveImage(MultipartFile file, String imageName){

        if (file == null || file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        Path path = resolvePath(imageName);

        try (InputStream inputStream = file.getInputStream()) {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeErrorException(null, "File" + imageName + "has not been saved");
        }
    }

    public boolean delete(String filename) throws IOException {
        Path file = rootLocation.resolve(filename);
        return Files.deleteIfExists(file);
    }

    @Override
    public Resource loadAsResource(String filename){
        try {
            Path file = resolvePath(filename);
            Resource resource = createUrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public String createUniqueName(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String baseName = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String combinedName = MessageFormat.format("{0}-{1}.{2}", baseName, time.checkCurrentTime(), fileExtension);

        return combinedName;
    }

    protected Resource createUrlResource(URI uri) throws MalformedURLException {
        return new UrlResource(uri);
    }
}