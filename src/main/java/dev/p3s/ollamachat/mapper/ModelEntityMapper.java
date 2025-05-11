package dev.p3s.ollamachat.mapper;

import dev.p3s.ollamachat.entity.Chat;
import dev.p3s.ollamachat.entity.Message;
import dev.p3s.ollamachat.entity.User;
import dev.p3s.ollamachat.model.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelEntityMapper {
    UserSummary toSummary(User user);
    UserDto toDto(User user);
    ChatSummary toSummary(Chat chat);
    ChatDto toDto(Chat chat);
    MessageSummary toSummary(Message message);
}
