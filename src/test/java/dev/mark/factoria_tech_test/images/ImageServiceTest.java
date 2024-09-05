package dev.mark.factoria_tech_test.images;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.mark.factoria_tech_test.config.StorageProperties;
import dev.mark.factoria_tech_test.users.profiles.Profile;
import dev.mark.factoria_tech_test.users.profiles.ProfileRepository;
import dev.mark.factoria_tech_test.users.security.SecurityUser;
import dev.mark.factoria_tech_test.utilities.FileOperations;
import dev.mark.factoria_tech_test.utilities.Time;

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private Time time;

    @Mock
    private MultipartFile file;

    @Mock
    private Authentication auth;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private StorageProperties storageProperties;

    @Mock
    private FileOperations fileOperations;

    private ImageService imageService;

    private final Path rootLocation = Paths.get("uploads");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(storageProperties.getLocation()).thenReturn("uploads");

        imageService = new ImageService(imageRepository, time, storageProperties, profileRepository, fileOperations);

        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void init_ShouldCreateDirectories() throws IOException {
        imageService.init();
        assertTrue(Files.exists(rootLocation));
    }

    @Test
    void resolvePath_ShouldReturnCorrectPath() {
        String filename = "testImage.jpg";
        Path resolvedPath = imageService.resolvePath(filename);
        assertEquals(rootLocation.resolve(filename), resolvedPath);
    }

    @Test
    void saveImage_ShouldThrowException_WhenFileIsEmpty() throws IOException {
        when(file.isEmpty()).thenReturn(true);

        StorageException thrown = assertThrows(StorageException.class, () -> {
            imageService.saveImage(file, "Test Image");
        });

        assertEquals("Failed to store empty file.", thrown.getMessage());
    }

    @Test
    void saveImage_ShouldSaveImage_WhenFileIsValid() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(file.getInputStream()).thenReturn(mock(InputStream.class));

        when(time.checkCurrentTime()).thenReturn("20230905123000");

        SecurityUser securityUser = mock(SecurityUser.class);
        when(securityUser.getId()).thenReturn(1L);
        when(auth.getPrincipal()).thenReturn(securityUser);

        Profile profile = new Profile();
        profile.setImages(new ArrayList<>());
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        imageService.saveImage(file, "Test Image");

        verify(imageRepository, times(1)).save(any(Image.class));
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
void loadAsResource_ShouldReturnResource_WhenFileExists() throws Exception {
    String filename = "image.jpg";

    ImageService spyImageService = spy(imageService);

    Path mockPath = mock(Path.class);
    when(spyImageService.resolvePath(filename)).thenReturn(mockPath);

    URI mockUri = new URI("file:/path/to/image.jpg");
    when(mockPath.toUri()).thenReturn(mockUri);

    UrlResource mockResource = mock(UrlResource.class);
    when(mockResource.exists()).thenReturn(true);
    when(mockResource.isReadable()).thenReturn(true);
    
    doReturn(mockResource).when(spyImageService).createUrlResource(mockUri);

    Resource returnedResource = spyImageService.loadAsResource(filename);

    assertNotNull(returnedResource);
    assertSame(mockResource, returnedResource);
    verify(mockResource, times(1)).exists();
}

    @Test
    void loadAsResource_ShouldThrowException_WhenFileDoesNotExist() {
        String filename = "nonExistent.jpg";

        StorageFileNotFoundException thrown = assertThrows(StorageFileNotFoundException.class, () -> {
            imageService.loadAsResource(filename);
        });

        assertEquals("Could not read file: nonExistent.jpg", thrown.getMessage());
    }

    @Test
    void delete_ShouldReturnTrue_WhenFileDeletedSuccessfully() throws IOException {
        String filename = "image.jpg";

        Image image = mock(Image.class);
        when(imageRepository.findByImageName(filename)).thenReturn(Optional.of(image));

        FileOperations fileOperations = mock(FileOperations.class);
        when(fileOperations.deleteIfExists(rootLocation.resolve(filename))).thenReturn(true);

        ImageService imageServiceWithMock = new ImageService(imageRepository, time, storageProperties, profileRepository, fileOperations);

        boolean result = imageServiceWithMock.delete(filename);
        assertTrue(result);

        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    void delete_ShouldThrowException_WhenImageNotFound() {
        String filename = "nonExistent.jpg";

        when(imageRepository.findByImageName(filename)).thenReturn(Optional.empty());

        StorageFileNotFoundException thrown = assertThrows(StorageFileNotFoundException.class, () -> {
            imageService.delete(filename);
        });

        assertEquals("Image not found in the database", thrown.getMessage());
    }

    @Test
    void createUniqueName_ShouldReturnUniqueName() {
        when(file.getOriginalFilename()).thenReturn("example.jpg");

        when(time.checkCurrentTime()).thenReturn("20230905123000");

        String uniqueName = imageService.createUniqueName(file);

        assertEquals("example-20230905123000.jpg", uniqueName);
    }
}