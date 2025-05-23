package dev.p3s.ollamachat.repository;

import dev.p3s.ollamachat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByUserId(UUID userId);
}
