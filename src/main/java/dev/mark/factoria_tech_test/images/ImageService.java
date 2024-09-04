package dev.mark.factoria_tech_test.images;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

import javax.management.RuntimeErrorException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import dev.mark.factoria_tech_test.config.StorageProperties;
import dev.mark.factoria_tech_test.utilities.Time;

@Service
public class ImageService implements IStorageService {

    ImageRepository imageRepository;
    Time time;
    private final Path rootLocation;

    public ImageService(ImageRepository imageRepository, Time time, StorageProperties properties) throws StorageException {
        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.imageRepository = imageRepository;
        this.time = time;
    }

    @Override
    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public Path createPath(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void saveImage(MultipartFile file, @NonNull String imageTitle) throws StorageException {

        if (file != null) {
            String uniqueName = createUniqueName(file);
            Path path = createPath(uniqueName);

            Image newImage = Image.builder().imageTitle(imageTitle).imageName(uniqueName).build();

            try (InputStream inputStream = file.getInputStream()) {
                if (file.isEmpty()) {
                    throw new StorageException("Failed to store empty file.");
                }
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                imageRepository.save(newImage);
            } catch (IOException e) {
                throw new RuntimeErrorException(null, "File" + uniqueName + "has not been saved");
            }

        }

    }

    @Override
    public Resource loadAsResource(String filename) throws StorageException {
        try {
            Path file = createPath(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Image image = imageRepository.findByImageName(filename)
                    .orElseThrow(() -> new FileNotFoundException("Image not found in the database"));
            imageRepository.delete(image);
            Path file = rootLocation.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String createUniqueName(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String baseName = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String combinedName = MessageFormat.format("{0}-{1}.{2}", baseName, time.checkCurrentTime(), fileExtension);

        return combinedName;
    }
}