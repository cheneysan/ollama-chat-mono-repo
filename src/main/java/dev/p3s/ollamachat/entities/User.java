package dev.p3s.ollamachat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users") // automatic naming is `user` but that causes SQL errors in H2 (reserved word)
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Version
    private Integer version;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String encryptedPassword;

    @NotNull
    @NotBlank
    private String displayName;

    private boolean enabled;

    @NotNull
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Chat> chats;

}
