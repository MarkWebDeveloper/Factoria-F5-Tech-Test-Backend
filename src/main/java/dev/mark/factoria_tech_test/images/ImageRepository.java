package dev.mark.factoria_tech_test.images;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
    public Optional<Image> findByImageName(String name);
}