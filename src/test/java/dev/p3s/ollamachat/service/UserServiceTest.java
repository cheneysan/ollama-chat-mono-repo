package dev.p3s.ollamachat.service;

import dev.p3s.ollamachat.entity.User;
import dev.p3s.ollamachat.mapper.ModelEntityMapper;
import dev.p3s.ollamachat.model.UserSummary;
import dev.p3s.ollamachat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ModelEntityMapper modelEntityMapper;

    @Autowired
    private UserService userService;

    @Test
    void testRegisterUser_UnknownEmail_ShouldReturnNewUserSummary() {
        String email = "test@test.com";
        String password = "password";
        String displayName = "Test User";
        User user = new User(); // Mock the User object creation
        UserSummary userSummary = new UserSummary(UUID.randomUUID(), email, displayName, LocalDateTime.now());

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelEntityMapper.toSummary(user)).thenReturn(userSummary); // Map mocked User to UserSummary

        Optional<UserSummary> result = userService.registerUser(email, password, displayName);

        assertTrue(result.isPresent());
        assertEquals(userSummary, result.get());
        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
        verify(modelEntityMapper, times(1)).toSummary(user);
    }

    @Test
    void testRegisterUser_ExistingEmail_ShouldReturnEmptyOptional() {
        String email = "test@test.com";
        String password = "password";
        String displayName = "Test User";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        Optional<UserSummary> result = userService.registerUser(email, password, displayName);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, never()).save(any(User.class));
        verify(modelEntityMapper, never()).toSummary(any(User.class));
    }

    @Test
    void testGetUserById_ExistingId_ShouldReturnUserSummary() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        UserSummary userSummary = new UserSummary(userId, "test@test.com", "Test User", LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelEntityMapper.toSummary(user)).thenReturn(userSummary);

        Optional<UserSummary> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userSummary, result.get());
        verify(userRepository, times(1)).findById(userId);
        verify(modelEntityMapper, times(1)).toSummary(user);
    }

    @Test
    void testGetUserByIdWith_UnknownId_ShouldReturnEmptyOptional() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserSummary> result = userService.getUserById(userId);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
        verify(modelEntityMapper, never()).toSummary(any(User.class));
    }

    @Test
    void testGetUserByEmail_ExistingEmail_ShouldReturnUserSummary() {
        String email = "test@test.com";
        User user = new User();
        UserSummary userSummary = new UserSummary(UUID.randomUUID(), email, "Test User", LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(modelEntityMapper.toSummary(user)).thenReturn(userSummary);

        Optional<UserSummary> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(userSummary, result.get());
        verify(userRepository, times(1)).findByEmail(email);
        verify(modelEntityMapper, times(1)).toSummary(user);
    }

    @Test
    void testGetUserByEmail_WithUnknownEmail_ShouldReturnEmptyOptional() {
        String email = "test@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<UserSummary> result = userService.getUserByEmail(email);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
        verify(modelEntityMapper, never()).toSummary(any(User.class));
    }

    @Test
    void testUpdateUserByEmail_ExistingEmail_ShouldReturnUpdatedUserSummary() {
        String email = "test@test.com";
        String newDisplayName = "Updated User";
        User user = new User();
        user.setEmail(email);
        UserSummary userSummary = new UserSummary(UUID.randomUUID(), email, newDisplayName, LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(modelEntityMapper.toSummary(user)).thenReturn(userSummary);

        Optional<UserSummary> result = userService.updateUserByEmail(email, newDisplayName);

        assertTrue(result.isPresent());
        assertEquals(newDisplayName, user.getDisplayName());
        assertEquals(userSummary, result.get());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(user);
        verify(modelEntityMapper, times(1)).toSummary(user);
    }

    @Test
    void testUpdateUserByEmail_UnknownEmail_ShouldReturnEmptyOptional() {
        String email = "test@test.com";
        String newDisplayName = "Updated User";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<UserSummary> result = userService.updateUserByEmail(email, newDisplayName);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
        verify(modelEntityMapper, never()).toSummary(any(User.class));
    }
}