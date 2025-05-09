package dev.p3s.ollamachat.repositories;

import dev.p3s.ollamachat.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByUserId(UUID userId);
}
