package dev.p3s.ollamachat.service;

import dev.p3s.ollamachat.entity.User;
import dev.p3s.ollamachat.mapper.ModelEntityMapper;
import dev.p3s.ollamachat.model.UserDto;
import dev.p3s.ollamachat.model.UserSummary;
import dev.p3s.ollamachat.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelEntityMapper modelEntityMapper;

    @Override
    public Optional<UserSummary> registerUser(String email, String encrypted_password, String displayName) {
        if (userRepository.existsByEmail(email)) {
            return Optional.empty();
        }

        User user = User.builder()
                .email(email)
                .encryptedPassword(encrypted_password)
                .displayName(displayName)
                .createdDate(LocalDateTime.now())
                .enabled(true)
                .build();

        return Optional.of(modelEntityMapper.toSummary(userRepository.save(user)));
    }

    @Override
    public Optional<UserSummary> getUserById(UUID userId) {
        return userRepository.findById(userId).map(modelEntityMapper::toSummary);
    }

    @Override
    public Optional<UserSummary> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(modelEntityMapper::toSummary);
    }

    @Override
    public Optional<UserDto> getFullUserByEmail(String email) {
        return userRepository.findByEmail(email).map(modelEntityMapper::toDto);
    }

    @Override
    public Optional<UserSummary> updateUserByEmail(String email, String displayName) {
        AtomicReference<Optional<UserSummary>> ref = new AtomicReference<>(Optional.empty());
        userRepository.findByEmail(email).ifPresent(u -> {
            u.setDisplayName(displayName);
            ref.set(Optional.of(modelEntityMapper.toSummary(userRepository.save(u))));
        });
        return ref.get();
    }
}
