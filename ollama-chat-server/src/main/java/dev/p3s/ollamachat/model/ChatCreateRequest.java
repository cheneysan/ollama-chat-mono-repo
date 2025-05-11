package dev.p3s.ollamachat.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatCreateRequest {

    @NotNull(message = "Chat title is required")
    @NotBlank(message = "Chat title is required")
    @Size(max = 255, message = "Chat title must be at most 255 characters long")
    private String title;

    @NotNull(message = "Initial message is required")
    @NotBlank(message = "Initial message is required")
    @Size(max = 65536, message = "Initial message must be at most 65536 characters long")
    private String message;

}
