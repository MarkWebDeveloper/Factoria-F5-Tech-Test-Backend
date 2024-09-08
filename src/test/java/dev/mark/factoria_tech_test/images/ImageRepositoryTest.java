package dev.mark.factoria_tech_test.images;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void findAll_ShouldThrowException() {
        assertThrows(UnsupportedOperationException.class, () -> repository.findAll());
    }

    @Test
    void findById_ShouldGetOneById() {

        Image newImage = Image.builder().imageName("New Image").build();
        entityManager.persist(newImage);
        Image searchedImage = repository.findById(newImage.getId()).orElseThrow();

        assertEquals(newImage.getId(), searchedImage.getId());
    }

    @Test
    void findByName_ShouldGetOneByName() {

        Image image = Image.builder().imageName("Test Image").imageTitle("Test Title").build();

        entityManager.persist(image);
        Image searchedProduct = repository.findByImageName("Test Image").orElseThrow();

        assertEquals("Test Image", searchedProduct.getImageName());
    }

    @Test
    void delete_ShouldDeleteOneById() {

        Image newImage = Image.builder().imageName("Image to delete").build();
        Image savedImage = repository.save(newImage);
        repository.deleteById(savedImage.getId());
        Optional<Image> deletedProduct = repository.findById(savedImage.getId());
        
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    void save_ShouldSaveNewImage() {

        Image newImage = Image.builder().imageName("New Image").imageTitle("New Title").build();
        Image savedImage = repository.save(newImage);
        Image searchedImage = repository.findByImageName("New Image").orElseThrow();

        assertEquals(savedImage.getImageName(), newImage.getImageName());
        assertEquals(savedImage.getImageTitle(), newImage.getImageTitle());
        assertEquals(savedImage.getImageName(), searchedImage.getImageName());
    }

    @Test
    void delete_shouldDeleteSavedImage() {

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
