package dev.mark.factoria_tech_test.images;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image,Long> {
    public Optional<Image> findByImageName(String name);

    default List<Image> findAll() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }
    
    @Query("SELECT i FROM Image i JOIN i.profiles p WHERE p.id = :profileId")
    public Optional<List<Image>> findImagesByProfileId(@Param("profileId") Long profileId);
}