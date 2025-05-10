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
public class UserSummary {
    private UUID id;
    private String email;
    private String displayName;
    private LocalDateTime createdDate;
}
