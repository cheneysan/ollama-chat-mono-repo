package dev.p3s.ollamachat.repositories;

import dev.p3s.ollamachat.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(@NotNull @NotBlank String email);
}
