package dev.p3s.ollamachat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Version
    private Integer version;

    private UUID chatId;

    @NotNull
    @NotEmpty
    private String text;

    @NotNull
    private MessageSender sender;

    @NotNull
    private LocalDateTime creationDate;

}
