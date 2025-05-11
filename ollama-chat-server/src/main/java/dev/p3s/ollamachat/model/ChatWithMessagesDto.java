package dev.p3s.ollamachat.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ChatWithMessagesDto {
    private UUID id;
    private UUID userId;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private LocalDateTime lastModifiedDate;

    List<MessageSummary> messages;
}
