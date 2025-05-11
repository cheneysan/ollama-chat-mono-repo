package dev.p3s.ollamachat.service;

import dev.p3s.ollamachat.entity.MessageSender;
import dev.p3s.ollamachat.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatService {

    List<ChatDto> getChatsForUser(UUID userId);

    Optional<ChatDto> getChatById(UUID chatId);

    Optional<ChatWithMessagesDto> getChatWithMessagesById(UUID chatId);

    ChatDto createChat(UUID userId, String title, String initialMessage);

    MessageSummary addChatMessage(UUID id, String message, Sender sender);

    boolean deleteChatById(UUID id);

}
