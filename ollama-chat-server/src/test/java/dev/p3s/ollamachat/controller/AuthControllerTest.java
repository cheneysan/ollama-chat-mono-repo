package dev.p3s.ollamachat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.p3s.ollamachat.component.JwtUtil;
import dev.p3s.ollamachat.config.SecurityConfig;
import dev.p3s.ollamachat.model.AuthenticationRequest;
import dev.p3s.ollamachat.model.UserCreateRequest;
import dev.p3s.ollamachat.model.UserSummary;
import dev.p3s.ollamachat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)

//@SpringBootTest
//@AutoConfigureMockMvc
class AuthControllerTest {

    @MockitoBean
    private AuthenticationManager authManager;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ChatController chatController;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testLogin() throws Exception {
        var authRequest = new AuthenticationRequest("test@test.com", "abcd1234");
        var userSummary = UserSummary.builder()
                .email("test@test.com")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build();
        var token = "testToken";

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userService.getUserByEmail(authRequest.getEmail())).thenReturn(Optional.of(userSummary));
        when(jwtUtil.generateToken(userSummary.getEmail())).thenReturn(token);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        var authRequest = new AuthenticationRequest("test@test.com", "abcd1234");
        var userSummary = UserSummary.builder()
                .email("test@test.com")
                .displayName("Test User")
                .createdDate(LocalDateTime.now())
                .build();
        var token = "testToken";

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userService.getUserByEmail(authRequest.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(userSummary.getEmail())).thenReturn(token);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterUser() throws Exception {
        var request = new UserCreateRequest("test@test.com", "abcd1234", "Test User");
        var summary = new UserSummary(null, "test@test.com", "Test User", LocalDateTime.now());

        when(userService.registerUser(eq(request.getEmail()), any(String.class), eq(request.getDisplayName()))).thenReturn(Optional.of(summary));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUserWithExistingEmail() throws Exception {
        var request = new UserCreateRequest("test@test.com", "abcd1234", "Test User");

        when(userService.registerUser(eq(request.getEmail()), any(String.class), eq(request.getDisplayName()))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


}