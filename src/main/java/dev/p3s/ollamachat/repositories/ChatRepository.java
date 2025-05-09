package dev.p3s.ollamachat.repositories;

import dev.p3s.ollamachat.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
    List<ChatEntity> findAllByUserId(UUID userId);
}
