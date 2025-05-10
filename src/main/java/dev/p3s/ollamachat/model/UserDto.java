package dev.p3s.ollamachat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private String encryptedPassword;
    private String displayName;
    private boolean enabled;
}
