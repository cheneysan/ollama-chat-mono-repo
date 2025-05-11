package dev.p3s.ollamachat.controller;

import dev.p3s.ollamachat.error.ChatNotFoundException;
import dev.p3s.ollamachat.error.UnauthorizedException;
import dev.p3s.ollamachat.error.UserNotFoundException;
import dev.p3s.ollamachat.model.*;
import dev.p3s.ollamachat.service.ChatService;
import dev.p3s.ollamachat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final UserService userService;
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatDto> createNewChat(@RequestBody ChatCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getCredentials().toString();

        UUID userId = userService.getUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException()).getId();
        ChatDto newChat = chatService.createChat(userId, request.getTitle(), request.getMessage());

        return ResponseEntity.ok(newChat);
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> getUserChats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getCredentials().toString();

        UUID userId = userService.getUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException()).getId();
        List<ChatDto> chats = chatService.getChatsForUser(userId);

        return ResponseEntity.ok(chats);
    }

    @PostMapping("/{id}")
    public ResponseEntity<MessageSummary> sendMessage(@PathVariable UUID id, @RequestBody SendMessageRequest request) {
        ChatDto chat = getChat(id);
        chatService.addChatMessage(chat.getId(), request.getMessage(), Sender.USER);

        // TODO send to Ollama and wait for a response
        String response = "Ok then...";

        MessageSummary msg = chatService.addChatMessage(id, response, Sender.OLLAMA);

        return ResponseEntity.ok(msg);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatWithMessagesDto> getChatWithMessages(@PathVariable UUID id) {
        ChatDto chat = getChat(id);
        return ResponseEntity.ok(chatService.getChatWithMessagesById(chat.getId()).get()); // get is safe as we've already checked the chat exists
    }

    private ChatDto getChat(UUID chatId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getCredentials().toString();
        UUID userId = userService.getUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException()).getId();
        ChatDto chat = chatService.getChatById(chatId).orElseThrow(() -> new ChatNotFoundException());
        if (!chat.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not allowed to access this chat");
        }
        return chat;
    }

}
