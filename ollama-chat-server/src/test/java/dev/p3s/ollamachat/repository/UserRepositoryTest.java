package dev.p3s.ollamachat.repository;

import dev.p3s.ollamachat.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = User.builder()
                .email("test@test.com")
                .encryptedPassword("test123")
                .displayName("Test User")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    void testSaveUserWithMissingFields() {
        User user = User.builder().build();
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.flush());
    }

    @Test
    void testFindUserByEmail() {
        User user = User.builder()
                .email("test@test.com")
                .encryptedPassword("test123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        Optional<User> foundUserResult = userRepository.findByEmail(savedUser.getEmail());

        assertThat(foundUserResult).isPresent();
        assertThat(foundUserResult.get().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void testFindUnknownByEmail() {
        Optional<User> foundUserResult = userRepository.findByEmail("nobody@test.com");
        assertThat(foundUserResult).isNotPresent();
    }

    @Test
    void testUpdateUser() {
        User user = userRepository.save(User.builder()
                .email("test@test.com")
                .encryptedPassword("test123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build());

        user.setDisplayName("Updated Test User");

        User updatedUser = userRepository.save(user);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(user.getId());
        assertThat(updatedUser.getDisplayName()).isEqualTo("Updated Test User");
    }

    @Test
    void testDeleteUser() {
        User user = userRepository.save(User.builder()
                .email("test@test.com")
                .encryptedPassword("test123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build());

        userRepository.delete(user);

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

}