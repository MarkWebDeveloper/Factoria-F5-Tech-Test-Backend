package dev.mark.factoria_tech_test.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);

    default List<User> findAll() {
        throw new UnsupportedOperationException("Unsupported operation. Please, use findById or findByEmail instead");
    }
}