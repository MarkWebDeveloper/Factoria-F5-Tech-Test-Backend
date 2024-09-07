package dev.mark.factoria_tech_test.images;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.mark.factoria_tech_test.users.UsersManager;
import dev.mark.factoria_tech_test.users.profiles.Profile;
import dev.mark.factoria_tech_test.users.profiles.ProfileRepository;
import dev.mark.factoria_tech_test.utilities.FileOperations;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepository imageRepository;
    private ProfileRepository profileRepository;
    private UsersManager usersManager;
    private FileOperations fileOperations;

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

    public Image updateImage(Optional<MultipartFile> file, @NonNull Long imageId, @NonNull String imageTitle) {

        Image updatingImage = imageRepository.findById(imageId).orElseThrow(() -> new StorageFileNotFoundException("Image not found"));

        if (file.isPresent()) {
            MultipartFile imageToSave = file.get();

            String uniqueImageName = fileOperations.createUniqueName(imageToSave);

            fileOperations.saveImage(imageToSave, uniqueImageName);

            updatingImage.setImageName(uniqueImageName);
        }

        updatingImage.setImageTitle(imageTitle);
        
        Image updatedImage = imageRepository.save(updatingImage);

        return updatedImage;
    }

    public void delete(String filename) {

        Long principalId = usersManager.getCurrentUserId();
        Profile profile = profileRepository.findById(principalId).orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Image image = imageRepository.findByImageName(filename)
        .orElseThrow(() -> new StorageFileNotFoundException("Image not found in the database"));

        profile.getImages().remove(image);
        imageRepository.delete(image);
    }
}