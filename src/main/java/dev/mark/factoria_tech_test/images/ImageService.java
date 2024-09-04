package dev.mark.factoria_tech_test.images;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import dev.mark.factoria_tech_test.config.StorageProperties;
import dev.mark.factoria_tech_test.users.profiles.Profile;
import dev.mark.factoria_tech_test.users.profiles.ProfileRepository;
import dev.mark.factoria_tech_test.users.security.SecurityUser;
import dev.mark.factoria_tech_test.utilities.Time;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ImageService implements IStorageService {

    private ImageRepository imageRepository;
    private Time time;
    private final Path rootLocation;
    private ProfileRepository profileRepository;

    public ImageService(ImageRepository imageRepository, Time time, StorageProperties properties, ProfileRepository profileRepository){
        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.imageRepository = imageRepository;
        this.time = time;
        this.profileRepository = profileRepository;
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
    public void saveImage(MultipartFile file, @NonNull String imageTitle){

        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication auth = contextHolder.getAuthentication();
        Long principalId = 0L;

        if (auth.getPrincipal() instanceof SecurityUser securityUser) {
            principalId = securityUser.getId();
            System.out.println("The user id is:" + principalId);
        }

        Profile profile = profileRepository.findById(principalId).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        List<Image> profileImages = profile.getImages();

        if (file != null) {
            String uniqueName = createUniqueName(file);
            Path path = resolvePath(uniqueName);

            Image newImage = Image.builder().imageTitle(imageTitle).imageName(uniqueName).build();

            try (InputStream inputStream = file.getInputStream()) {
                if (file.isEmpty()) {
                    throw new StorageException("Failed to store empty file.");
                }
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                imageRepository.save(newImage);
                profileImages.add(newImage);
                profile.setImages(profileImages);
                profileRepository.save(profile);
            } catch (IOException e) {
                throw new RuntimeErrorException(null, "File" + uniqueName + "has not been saved");
            }

        }

    }

    @Override
    public Resource loadAsResource(String filename){
        try {
            Path file = resolvePath(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Image image = imageRepository.findByImageName(filename)
                    .orElseThrow(() -> new StorageFileNotFoundException("Image not found in the database"));
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