package dev.mark.factoria_tech_test.users.profiles;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    public Optional<Profile> findByEmail(String name);

    default List<Profile> findAll() {
        throw new UnsupportedOperationException("Unsupported operation. Please, use findById or findByEmail instead");
    }
}
