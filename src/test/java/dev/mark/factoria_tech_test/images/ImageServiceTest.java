package dev.mark.factoria_tech_test.images;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dev.mark.factoria_tech_test.users.UsersManager;
import dev.mark.factoria_tech_test.users.profiles.Profile;
import dev.mark.factoria_tech_test.users.profiles.ProfileRepository;
import dev.mark.factoria_tech_test.utilities.FileOperations;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock FileOperations fileOperations;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @Mock
    SecurityContextHolder securityContextHolder;

    @Mock
    UsersManager usersManager;

    @Mock
    Profile profile;

    @Mock 
    MultipartFile file;

    @Test
    public void test_getCurrentUserImages_ShouldReturnUserImages() {

        Long mockPrincipalId = 2L;
        List<Image> mockImages = new ArrayList<>();
        Optional<List<Image>> mockOptionalImages = Optional.of(mockImages);

        when(usersManager.getCurrentUserId()).thenReturn(mockPrincipalId);
        when(imageRepository.findImagesByProfileId(mockPrincipalId)).thenReturn(mockOptionalImages);
        
        List<Image> images = imageService.getCurrentUserImages();

        assertEquals(mockImages, images, "Returned images should match mocked data");
    }

    @Test
    public void test_saveImage_shouldSaveImage() {

        Long PrincipalId = 2L;
        Optional<Profile> OptionalProfile = Optional.of(profile);
        when(usersManager.getCurrentUserId()).thenReturn(PrincipalId);
        when(profileRepository.findById(PrincipalId)).thenReturn(OptionalProfile);

        String imageTitle = "Test title";
        String imageName = "Test name";
        Image imageToSave = Image.builder().imageName(imageName).imageTitle(imageTitle).build();
        when(imageRepository.save(any(Image.class))).thenReturn(imageToSave);
        
        Image savedImage = imageService.saveImage(file, imageTitle, imageName);

        assertNotNull(savedImage);
        assertEquals(imageToSave, savedImage, "Returned images should match mocked data");
    }

    @Test
    public void test_updateImage_shouldUpdateImage() {

        Long oldImageId = 1L;
        String oldImageTitle = "Old Image Title";
        String oldImageName = "Old Image Name";
        String uniqueImageName = oldImageName + "-123456789.jpg";
        String newImageTitle = "New Image Title";
        Image imageToUpdate = Image.builder().id(1L).imageName(oldImageName).imageTitle(oldImageTitle).build();

        when(imageRepository.findById(oldImageId)).thenReturn(Optional.of(imageToUpdate));
        when(fileOperations.createUniqueName(any(MultipartFile.class))).thenReturn(uniqueImageName);
        when(imageRepository.save(imageToUpdate)).thenReturn(imageToUpdate);

        Image updatedImage = imageService.updateImage(Optional.of(file), oldImageId, newImageTitle);

        assertNotNull(updatedImage);
        assertEquals(updatedImage.getId(), oldImageId);
        assertEquals(updatedImage.getImageTitle(), newImageTitle);
        assertEquals(updatedImage.getImageName(), uniqueImageName);
    }

    @Test
    public void test_deleteImage_shouldDeleteImage() {

        String filename = "image.jpg";
        Long principalId = 2L;

        when(usersManager.getCurrentUserId()).thenReturn(principalId);

        Image imageToDelete = Image.builder().id(1L).imageName(filename).build();
        Profile newProfile = new Profile();
        List<Image> imageList = new ArrayList<>();
        imageList.add(imageToDelete);
        newProfile.setImages(imageList);

        when(profileRepository.findById(principalId)).thenReturn(Optional.of(newProfile));
        when(imageRepository.findByImageName(filename)).thenReturn(Optional.of(imageToDelete));

        imageService.delete(filename);

        assertFalse(newProfile.getImages().contains(imageToDelete));
    }
}