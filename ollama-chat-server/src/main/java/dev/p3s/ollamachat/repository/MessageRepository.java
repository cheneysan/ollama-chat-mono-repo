package dev.p3s.ollamachat.repository;

import dev.p3s.ollamachat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByChatId(UUID chatId);
}
