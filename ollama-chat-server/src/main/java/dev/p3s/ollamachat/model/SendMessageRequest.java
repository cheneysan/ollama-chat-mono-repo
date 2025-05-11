package dev.p3s.ollamachat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SendMessageRequest {
    private String message;
}
