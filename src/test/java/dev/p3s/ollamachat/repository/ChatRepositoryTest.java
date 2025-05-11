package dev.p3s.ollamachat.repository;

import dev.p3s.ollamachat.entity.Chat;
import dev.p3s.ollamachat.entity.Message;
import dev.p3s.ollamachat.entity.MessageSender;
import dev.p3s.ollamachat.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    User user;

    @BeforeEach
    void createUser() {
        user = userRepository.save(User.builder()
                .email("test@test.com")
                .encryptedPassword("123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build());
    }

    @Test
    void testSaveChat() {
        Chat chat = Chat.builder()
                .title("Test Chat")
                .userId(user.getId())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Chat savedChat = chatRepository.save(chat);

        assertNotNull(savedChat);
        assertNotNull(savedChat.getId());
    }

    @Test
    void testSaveChatWithMissingFields() {
        Chat chat = Chat.builder().build();
        chatRepository.save(chat);
        assertThrows(ConstraintViolationException.class, () -> chatRepository.flush());
    }

    @Test
    void testSaveChatWithMessages() {
        Chat chat = buildTestChatWithMessages(user);

        Chat savedChat = chatRepository.save(chat);
        chatRepository.flush();

        System.out.println(savedChat);

        assertNotNull(savedChat);
        assertNotNull(savedChat.getId());
        assertEquals(2, savedChat.getMessages().size());
        assertNotNull(savedChat.getMessages().get(0).getId());
        assertNotNull(savedChat.getMessages().get(1).getId());
    }

    @Test
    void testUpdateChat() {
        Chat chat = chatRepository.save(buildTestChatWithMessages(user));

        chat.setTitle("Updated Test Chat");

        Chat updatedChat = chatRepository.save(chat);
        chatRepository.flush();

        assertEquals("Updated Test Chat", updatedChat.getTitle());
    }

    @Test
    void addMessageToChat() {
        Chat chat = chatRepository.save(buildTestChatWithMessages(user));

        UUID message1Id = chat.getMessages().get(0).getId();
        UUID message2Id = chat.getMessages().get(1).getId();

        chat.getMessages().add(Message.builder()
                .text("Test Message 3")
                .sender(MessageSender.USER)
                .creationDate(LocalDateTime.now())
                .build());

        Chat updatedChat = chatRepository.save(chat);
        chatRepository.flush();

        assertEquals(3, updatedChat.getMessages().size());
        assertNotNull(updatedChat.getMessages().get(0).getId());
        assertNotNull(updatedChat.getMessages().get(1).getId());
        assertNotNull(updatedChat.getMessages().get(2).getId());

        assertEquals(message1Id, updatedChat.getMessages().get(0).getId());
        assertEquals(message2Id, updatedChat.getMessages().get(1).getId());
    }

    @Test
    void testMessageOrdering() {
        Chat chat = chatRepository.save(buildTestChatWithMessages(user));
        chat.getMessages().add(buildTestMessage("Test Message 3", MessageSender.USER));
        chat.getMessages().add(buildTestMessage("Test Message 4", MessageSender.OLLAMA));
        chat.getMessages().add(buildTestMessage("Test Message 5", MessageSender.USER));

        Chat updatedChat = chatRepository.save(chat);
        chatRepository.flush();

        assertEquals(5, updatedChat.getMessages().size());
        assertEquals("Test Message 1", updatedChat.getMessages().get(0).getText());
        assertEquals("Test Message 2", updatedChat.getMessages().get(1).getText());
        assertEquals("Test Message 3", updatedChat.getMessages().get(2).getText());
        assertEquals("Test Message 4", updatedChat.getMessages().get(3).getText());
        assertEquals("Test Message 5", updatedChat.getMessages().get(4).getText());
    }

    @Test
    void testDeleteChat() {
        Chat chat = chatRepository.save(buildTestChatWithMessages(user));
        chatRepository.delete(chat);

        chatRepository.flush();

        assertFalse(chatRepository.findById(chat.getId()).isPresent());
    }

    @Test
    void testFindAllByUserId() {
        User user2 = userRepository.save(User.builder()
                .email("test@test.com")
                .encryptedPassword("test123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build());

        chatRepository.save(buildTestChatWithMessages(user));
        chatRepository.save(buildTestChatWithMessages(user2));
        chatRepository.save(buildTestChatWithMessages(user2));

        chatRepository.flush();

        assertEquals(3, chatRepository.findAll().size());
        assertEquals(1, chatRepository.findAllByUserId(user.getId()).size());
        assertEquals(2, chatRepository.findAllByUserId(user2.getId()).size());

    }

    private Chat buildTestChatWithMessages(User user) {
        return Chat.builder()
                .userId(user.getId())
                .title("Test Chat")
                .messages(new ArrayList<>(List.of(
                        Message.builder()
                                .text("Test Message 1")
                                .sender(MessageSender.USER)
                                .creationDate(LocalDateTime.now())
                                .build(),
                        Message.builder()
                                .text("Test Message 2")
                                .sender(MessageSender.OLLAMA)
                                .creationDate(LocalDateTime.now())
                                .build()
                )))
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    private Message buildTestMessage(String text, MessageSender sender) {
        return Message.builder()
                .text(text)
                .sender(sender)
                .creationDate(LocalDateTime.now())
                .build();
    }

}