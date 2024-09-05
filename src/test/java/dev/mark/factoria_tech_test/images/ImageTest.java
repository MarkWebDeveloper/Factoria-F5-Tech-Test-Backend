package dev.mark.factoria_tech_test.images;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import dev.mark.factoria_tech_test.users.profiles.Profile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "DATASOURCE_PASSWORD=1234",
	"DATASOURCE_USERNAME=name",
	"DEFAULT_ADMIN_NAME=name",
	"DEFAULT_ADMIN_PASSWORD=1234"
})
public class ImageTest {

    Image image;
    List<Image> images;
    Profile profile;

    {
        image = Image.builder()
                .imageName("image1.jpg")
                .imageTitle("A beautiful landscape")
                .build();
        
        images = new ArrayList<>();
        images.add(image);
        
        profile = Profile.builder()
                .images(images)
                .build();
    }

    @Test
    public void testImageIsCreated() {

        // Assert the created image properties
        assertNotNull(image);
        assertEquals("image1.jpg", image.getImageName());
        assertEquals("A beautiful landscape", image.getImageTitle());
        assertTrue(profile.getImages().contains(image));
    }

    @Test
    void testProductHas7Attributes() {
        Field[] fields = image.getClass().getDeclaredFields();
        assertEquals(fields.length, 4);
    }

    @Test
    public void testImageRelationshipWithProfileEstablished() {
        Image newImage = Image.builder().imageName("image2.jpg").imageTitle("New Image").build();
        profile.getImages().add(newImage);

        // Assert the relationship
        assertTrue(profile.getImages().contains(image));
    }
}