package dev.p3s.ollamachat.repositories;

import dev.p3s.ollamachat.entities.UserEntity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testSaveUser() {
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password("test123")
                .displayName("Test User")
                .build();

        UserEntity savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    void testSaveUserWithMissingFields() {
        UserEntity user = UserEntity.builder().build();
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.flush());
    }

    @Test
    void testFindUserByEmail() {
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password("test123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build();

        UserEntity savedUser = userRepository.save(user);

        UserEntity foundUser = userRepository.findByEmail(savedUser.getEmail());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void testFindUnknownByEmail() {
        UserEntity foundUser = userRepository.findByEmail("nobody@test.com");
        assertThat(foundUser).isNull();
    }

    @Test
    void testUpdateUser() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@test.com")
                .password("test123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build());

        user.setDisplayName("Updated Test User");

        UserEntity updatedUser = userRepository.save(user);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(user.getId());
        assertThat(updatedUser.getDisplayName()).isEqualTo("Updated Test User");
    }

    @Test
    void testDeleteUser() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@test.com")
                .password("test123")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build());

        userRepository.delete(user);

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

}