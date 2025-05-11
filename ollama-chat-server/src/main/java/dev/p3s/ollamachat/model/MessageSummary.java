package dev.p3s.ollamachat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSummary {
    private UUID id;
    private String text;
    private String sender;
    private LocalDateTime creationDate;
}
