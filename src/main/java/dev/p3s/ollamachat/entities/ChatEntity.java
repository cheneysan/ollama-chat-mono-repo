package dev.p3s.ollamachat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Version
    private Integer version;

    @NotNull
    private UUID userId;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatId", fetch = FetchType.EAGER)
    private List<MessageEntity> messages;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private LocalDateTime lastModifiedDate;

}
