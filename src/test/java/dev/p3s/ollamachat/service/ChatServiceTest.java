package dev.p3s.ollamachat.service;

import dev.p3s.ollamachat.entity.Chat;
import dev.p3s.ollamachat.entity.User;
import dev.p3s.ollamachat.error.UserNotFoundException;
import dev.p3s.ollamachat.mapper.ModelEntityMapper;
import dev.p3s.ollamachat.model.ChatDto;
import dev.p3s.ollamachat.model.ChatSummary;
import dev.p3s.ollamachat.repository.ChatRepository;
import dev.p3s.ollamachat.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ChatServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ChatRepository chatRepository;

    @MockitoBean
    private ModelEntityMapper modelEntityMapper;

    @Autowired
    private ChatService chatService;

    private UUID userId;
    private UUID chatId;
    private User user;
    private Chat chat;
    private ChatDto chatDto;
    private ChatSummary chatSummary;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        chatId = UUID.randomUUID();

        user = User.builder().id(userId).build();

        chat = Chat.builder()
                .id(chatId)
                .userId(userId)
                .title("Test Chat")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        chatDto = ChatDto.builder()
                .id(chatId)
                .title("Test Chat")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        chatSummary = ChatSummary.builder()
                .id(chatId)
                .title("Test Chat")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void getChatsForUser() {
        List<Chat> chats = List.of(chat);
        when(chatRepository.findAllByUserId(userId)).thenReturn(chats);
        when(modelEntityMapper.toDto(any(Chat.class))).thenReturn(chatDto);

        List<ChatDto> result = chatService.getChatsForUser(userId);

        assertEquals(1, result.size());
        assertEquals(chatDto, result.get(0));
        verify(chatRepository).findAllByUserId(userId);
        verify(modelEntityMapper, times(chats.size())).toDto(any(Chat.class));
    }

    @Test
    void getChatById() {
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(modelEntityMapper.toDto(chat)).thenReturn(chatDto);

        Optional<ChatDto> result = chatService.getChatById(chatId);

        assertTrue(result.isPresent());
        assertEquals(chatDto, result.get());
        verify(chatRepository).findById(chatId);
        verify(modelEntityMapper).toDto(any(Chat.class));
    }

    @Test
    void getChatById_notFound() {
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        Optional<ChatDto> result = chatService.getChatById(chatId);

        assertFalse(result.isPresent());
        verify(chatRepository).findById(chatId);
        verifyNoInteractions(modelEntityMapper);
    }

    @Test
    void createChat() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);
        when(modelEntityMapper.toDto(any(Chat.class))).thenReturn(chatDto);

        ChatDto result = chatService.createChat(userId, "New Chat", "Hello");

        assertEquals(chatDto, result);
        verify(userRepository).findById(userId);
        verify(chatRepository).save(any(Chat.class));
        verify(modelEntityMapper).toDto(any(Chat.class));
    }

    @Test
    void createChat_userNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> chatService.createChat(userId, "New Chat", "Hello"));
        verify(userRepository).findById(userId);
        verifyNoInteractions(chatRepository, modelEntityMapper);
    }

    @Test
    void deleteChatById() {
        when(chatRepository.existsById(chatId)).thenReturn(true);

        boolean result = chatService.deleteChatById(chatId);

        assertTrue(result);
        verify(chatRepository).existsById(chatId);
        verify(chatRepository).deleteById(chatId);
    }

    @Test
    void deleteChatById_notFound() {
        when(chatRepository.existsById(chatId)).thenReturn(false);

        boolean result = chatService.deleteChatById(chatId);

        assertFalse(result);
        verify(chatRepository).existsById(chatId);
        verify(chatRepository, never()).deleteById(chatId);
    }


}