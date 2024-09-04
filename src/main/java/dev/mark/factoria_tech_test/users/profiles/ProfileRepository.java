package dev.mark.factoria_tech_test.users.profiles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    default List<Profile> findAll() {
        throw new UnsupportedOperationException("Unsupported operation. Please, use findById or findByEmail instead");
    }
}
