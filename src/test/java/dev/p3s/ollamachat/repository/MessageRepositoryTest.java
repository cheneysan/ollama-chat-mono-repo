package dev.p3s.ollamachat.repository;

import dev.p3s.ollamachat.entity.Chat;
import dev.p3s.ollamachat.entity.Message;
import dev.p3s.ollamachat.entity.MessageSender;
import dev.p3s.ollamachat.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    User user;
    Chat chat;

    @BeforeEach
    void createChat() {
        user = userRepository.save(User.builder()
                .email("test@test.com")
                .encryptedPassword("123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build());

        chat = chatRepository.save(Chat.builder()
                .title("Test Chat")
                .userId(user.getId())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build());
    }

    @Test
    void testCreateAndRetrieveMessage() {
        Message message = messageRepository.save(Message.builder()
                .chatId(chat.getId())
                .text("Test message")
                .sender(MessageSender.USER)
                .creationDate(LocalDateTime.now())
                .build());

        Message found = messageRepository.findById(message.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(message.getText(), found.getText());
    }

    @Test
    void testFindMessagesByChatId() {
        messageRepository.save(Message.builder()
                .chatId(chat.getId())
                .text("Message 1")
                .sender(MessageSender.USER)
                .creationDate(LocalDateTime.now())
                .build());

        messageRepository.save(Message.builder()
                .chatId(chat.getId())
                .text("Message 2")
                .sender(MessageSender.OLLAMA)
                .creationDate(LocalDateTime.now())
                .build());

        List<Message> messages = messageRepository.findAllByChatId(chat.getId());
        assertEquals(2, messages.size());
    }

    @Test
    void testDeleteMessage() {
        Message message = messageRepository.save(Message.builder()
                .chatId(chat.getId())
                .text("Test message")
                .sender(MessageSender.USER)
                .creationDate(LocalDateTime.now())
                .build());

        messageRepository.deleteById(message.getId());
        assertTrue(messageRepository.findById(message.getId()).isEmpty());
    }

}