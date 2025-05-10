package dev.p3s.ollamachat.service;

import dev.p3s.ollamachat.model.UserSummary;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<UserSummary> registerUser(String email, String encrypted_password, String displayName);

    Optional<UserSummary> getUserById(UUID userId);

    Optional<UserSummary> getUserByEmail(String email);

    Optional<UserSummary> updateUserByEmail(String email, String displayName);

}
