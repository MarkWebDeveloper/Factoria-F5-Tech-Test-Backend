package dev.mark.factoria_tech_test.images;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.mark.factoria_tech_test.users.UsersManager;
import dev.mark.factoria_tech_test.users.profiles.Profile;
import dev.mark.factoria_tech_test.users.profiles.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepository imageRepository;
    private ProfileRepository profileRepository;
    private UsersManager usersManager;

    @PreAuthorize("hasRole('USER')")
    public List<Image> getCurrentUserImages() {

        Long principalId = usersManager.getCurrentUserId();

        List<Image> images = imageRepository.findImagesByProfileId(principalId).orElseThrow(() -> new StorageFileNotFoundException("Profile not found"));
        return images;
    }

    public Image saveImage(MultipartFile file, @NonNull String imageTitle, @NonNull String imageName) {

        Long principalId = usersManager.getCurrentUserId();

        Profile profile = profileRepository.findById(principalId).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        List<Image> profileImages = profile.getImages();

        Image newImage = Image.builder().imageTitle(imageTitle).imageName(imageName).build();

        Image savedImage = imageRepository.save(newImage);
        profileImages.add(newImage);
        profile.setImages(profileImages);
        profileRepository.save(profile);
        
        return savedImage;
    }

    @PreAuthorize("hasRole('USER')")
    public void delete(String filename) {
        Image image = imageRepository.findByImageName(filename)
                .orElseThrow(() -> new StorageFileNotFoundException("Image not found in the database"));
        imageRepository.delete(image);
    }
}