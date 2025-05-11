package dev.p3s.ollamachat.service;

import dev.p3s.ollamachat.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDto> userResult = userService.getFullUserByEmail(username);

        if (userResult.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto user = userResult.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getEncryptedPassword())
                .build();
    }
}
