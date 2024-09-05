package dev.mark.factoria_tech_test.images;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class ImageRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ImageRepository repository;

    @Test
    void testShouldGetAllImages() {

        Image image = Image.builder().imageName("Test Image").imageTitle("Test Title").build();
        Image otherImage = Image.builder().imageName("Other image").imageTitle("Other image title").build();
        
        entityManager.persist(image);
        entityManager.persist(otherImage);

        List<Image> images = repository.findAll();

        assertThat(images, hasSize(greaterThan(1)));
        assertThat(images.get(0).getImageName()).isEqualTo("Test Image");
    }

    @Test
    void testShouldGetOneImageById() {

        Image newImage = Image.builder().imageName("New Image").build();
        entityManager.persist(newImage);
        Image searchedImage = repository.findById(newImage.getId()).orElseThrow();

        assertEquals(newImage.getId(), searchedImage.getId());
    }

    @Test
    void testShouldGetAnImageByName() {

        Image image = Image.builder().imageName("Test Image").imageTitle("Test Title").build();

        entityManager.persist(image);
        Image searchedProduct = repository.findByImageName("Test Image").orElseThrow();

        assertEquals("Test Image", searchedProduct.getImageName());
    }

    @Test
    void testDeleteImageById() {

        Image newImage = Image.builder().imageName("Image to delete").build();
        Image savedImage = repository.save(newImage);
        repository.deleteById(savedImage.getId());
        Optional<Image> deletedProduct = repository.findById(savedImage.getId());
        
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    void testShouldSaveAndReturnNewProduct() {

        Image newImage = Image.builder().imageName("New Image").imageTitle("New Title").build();
        Image savedImage = repository.save(newImage);
        Image searchedImage = repository.findByImageName("New Image").orElseThrow();

        assertEquals(savedImage.getImageName(), newImage.getImageName());
        assertEquals(savedImage.getImageTitle(), newImage.getImageTitle());
        assertEquals(savedImage.getImageName(), searchedImage.getImageName());
    }

    @Test
    void testShouldDeleteAnExistingImage() {

        Image newImage = Image.builder().imageName("Image to delete").build();
        entityManager.persist(newImage);

        repository.delete(newImage);

        Optional<Image> deletedImage = repository.findByImageName("Image to delete");
        
        assertFalse(deletedImage.isPresent());
    }

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

}
