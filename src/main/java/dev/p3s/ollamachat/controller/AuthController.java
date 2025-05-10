package dev.p3s.ollamachat.controller;

import dev.p3s.ollamachat.component.JwtUtil;
import dev.p3s.ollamachat.error.AuthenticationException;
import dev.p3s.ollamachat.model.AuthenticationRequest;
import dev.p3s.ollamachat.model.AuthenticationResponse;
import dev.p3s.ollamachat.model.UserCreateRequest;
import dev.p3s.ollamachat.model.UserSummary;
import dev.p3s.ollamachat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        } catch (BadCredentialsException e) {
            log.trace("Bad credentials", e);
            throw new AuthenticationException("Invalid email or password");
        }

        final UserSummary userSummary = userService.getUserByEmail(request.getEmail()).orElseThrow(AuthenticationException::new);
        final String token = jwtUtil.generateToken(userSummary.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateRequest request) {
        var encryptedPassword = passwordEncoder.encode(request.getPassword());
        var result = userService.registerUser(request.getEmail(), encryptedPassword, request.getDisplayName());
        if (result.isEmpty()) {
            log.error("Registration failed - user with email {} already exists", request.getEmail());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


}
