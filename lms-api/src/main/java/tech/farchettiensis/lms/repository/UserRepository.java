package tech.farchettiensis.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.farchettiensis.lms.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
