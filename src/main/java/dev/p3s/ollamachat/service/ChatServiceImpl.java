package dev.p3s.ollamachat.service;

import dev.p3s.ollamachat.entity.Chat;
import dev.p3s.ollamachat.entity.Message;
import dev.p3s.ollamachat.entity.MessageSender;
import dev.p3s.ollamachat.entity.User;
import dev.p3s.ollamachat.error.ChatNotFoundException;
import dev.p3s.ollamachat.error.UserNotFoundException;
import dev.p3s.ollamachat.mapper.ModelEntityMapper;
import dev.p3s.ollamachat.model.ChatDto;
import dev.p3s.ollamachat.model.ChatWithMessagesDto;
import dev.p3s.ollamachat.model.MessageSummary;
import dev.p3s.ollamachat.model.Sender;
import dev.p3s.ollamachat.repository.ChatRepository;
import dev.p3s.ollamachat.repository.MessageRepository;
import dev.p3s.ollamachat.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ModelEntityMapper modelEntityMapper;
    private final MessageRepository messageRepository;

    @Override
    public List<ChatDto> getChatsForUser(UUID userId) {
        return chatRepository.findAllByUserId(userId).stream()
                .map(modelEntityMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ChatDto> getChatById(UUID chatId) {
        return chatRepository.findById(chatId).map(modelEntityMapper::toDto);
    }

    @Override
    public Optional<ChatWithMessagesDto> getChatWithMessagesById(UUID chatId) {
        return chatRepository.findById(chatId).map(chat -> {
            List<MessageSummary> messages = messageRepository.findAllByChatId(chatId).stream()
                    .map(modelEntityMapper::toSummary)
                    .toList();

            return ChatWithMessagesDto.builder()
                    .id(chat.getId())
                    .title(chat.getTitle())
                    .messages(messages)
                    .createdDate(chat.getCreatedDate())
                    .lastModifiedDate(chat.getLastModifiedDate())
                    .build();
        });
    }

    @Override
    public ChatDto createChat(UUID userId, String title, String initialMessage) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Chat chat = Chat.builder()
                .title(title)
                .userId(user.getId())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        chatRepository.save(chat);
        return modelEntityMapper.toDto(chat);
    }

//    @Override
//    public Optional<ChatSummary> updateChatById(UUID id, ChatSummary chatSummary) {
//        AtomicReference<Optional<ChatSummary>> ref = new AtomicReference<>(Optional.empty());
//        chatRepository.findById(id).ifPresent(entity -> {
//            entity.setTitle(chatSummary.getTitle());
//            entity.setLastModifiedDate(chatSummary.getLastModifiedDate());
//            ref.set(Optional.of(modelEntityMapper.toSummary(chatRepository.save(entity))));
//        });
//        return ref.get();
//    }

//    @Override
//    public Optional<ChatSummary> patchChatById(UUID id, ChatSummary chatSummary) {
//        AtomicReference<Optional<ChatSummary>> ref = new AtomicReference<>(Optional.empty());
//        chatRepository.findById(id).ifPresent(entity -> {
//            if (chatSummary.getTitle() != null) {
//                entity.setTitle(chatSummary.getTitle());
//            }
//            entity.setLastModifiedDate(chatSummary.getLastModifiedDate());
//            ref.set(Optional.of(modelEntityMapper.toSummary(chatRepository.save(entity))));
//        });
//        return ref.get();
//    }


    @Override
    public MessageSummary addChatMessage(UUID id, String message, Sender sender) {
        Chat chat = chatRepository.findById(id).orElseThrow(ChatNotFoundException::new);
        Message msg = Message.builder()
                .chatId(chat.getId())
                .sender(sender == Sender.USER ? MessageSender.USER : MessageSender.OLLAMA)
                .text(message)
                .creationDate(LocalDateTime.now())
                .build();

        messageRepository.save(msg);
        return modelEntityMapper.toSummary(msg);
    }

    @Override
    public boolean deleteChatById(UUID id) {
        if (chatRepository.existsById(id)) {
            chatRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
