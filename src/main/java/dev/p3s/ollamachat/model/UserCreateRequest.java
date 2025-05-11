package dev.p3s.ollamachat.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a data transfer object for creating a new user.
 * <p>
 * This class is used to encapsulate the information required to register a new user,
 * including their email address, password, and display name. The fields in this class
 * are validated to ensure that the provided data meets the necessary requirements.
 * <p>
 * The email field represents the user's email address and must:
 * - Be provided (not null or blank).
 * - Be a valid email address.
 * <p>
 * The password field represents the user's chosen password and must:
 * - Be provided (not null or blank).
 * - Be at least 8 characters long.
 * <p>
 * The displayName field represents the name the user wishes to display and must:
 * - Be provided (not null or blank).
 * - Be at least 3 characters long.
 */
@Data
@Builder
@AllArgsConstructor
public class UserCreateRequest {

    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Display name is required")
    @NotBlank(message = "Display name is required")
    @Size(min = 3, message = "Display name must be at least 3 characters long")
    private String displayName;
}
