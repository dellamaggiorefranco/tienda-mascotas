package com.mascotas.backend.modules.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mascotas.backend.modules.auth.dto.LoginRequest;
import com.mascotas.backend.modules.auth.dto.LoginResponse;
import com.mascotas.backend.modules.auth.dto.RegisterRequest;
import com.mascotas.backend.modules.auth.dto.RegisterResponse;
import com.mascotas.backend.modules.auth.exception.InvalidCredentialsException;
import com.mascotas.backend.modules.users.entity.User;
import com.mascotas.backend.modules.users.entity.UserRole;
import com.mascotas.backend.modules.users.exception.EmailAlreadyExistsException;
import com.mascotas.backend.modules.users.exception.UserNotFoundException;
import com.mascotas.backend.modules.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService, passwordEncoder, jwtService);
    }

    @Test
    void registerHashesPasswordAndCreatesCustomer() {
        RegisterRequest request = new RegisterRequest("juan@example.com", "password123");
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");

        User savedUser = User.builder()
                .id(1L)
                .email("juan@example.com")
                .passwordHash("hashed-password")
                .role(UserRole.CUSTOMER)
                .build();
        when(userService.create("juan@example.com", "hashed-password", UserRole.CUSTOMER)).thenReturn(savedUser);

        RegisterResponse response = authService.register(request);

        assertEquals(1L, response.id());
        assertEquals("juan@example.com", response.email());
        assertEquals(UserRole.CUSTOMER, response.role());
        verify(userService).create("juan@example.com", "hashed-password", UserRole.CUSTOMER);
    }

    @Test
    void registerPropagatesEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("juan@example.com", "password123");
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userService.create("juan@example.com", "hashed-password", UserRole.CUSTOMER))
                .thenThrow(new EmailAlreadyExistsException("juan@example.com"));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void loginWithUnknownEmailThrowsInvalidCredentials() {
        LoginRequest request = new LoginRequest("desconocido@example.com", "password123");
        when(userService.findByEmail("desconocido@example.com"))
                .thenThrow(new UserNotFoundException("desconocido@example.com"));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void loginWithWrongPasswordThrowsInvalidCredentials() {
        LoginRequest request = new LoginRequest("juan@example.com", "password-incorrecta");
        User user = User.builder()
                .id(1L)
                .email("juan@example.com")
                .passwordHash("hashed-password")
                .role(UserRole.CUSTOMER)
                .build();
        when(userService.findByEmail("juan@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password-incorrecta", "hashed-password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void loginWithCorrectCredentialsReturnsToken() {
        LoginRequest request = new LoginRequest("juan@example.com", "password123");
        User user = User.builder()
                .id(1L)
                .email("juan@example.com")
                .passwordHash("hashed-password")
                .role(UserRole.CUSTOMER)
                .build();
        when(userService.findByEmail("juan@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);
        when(jwtService.generateAccessToken("juan@example.com", UserRole.CUSTOMER)).thenReturn("token-generado");
        when(jwtService.getAccessTokenExpirationSeconds()).thenReturn(900L);

        LoginResponse response = authService.login(request);

        assertEquals("token-generado", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(900L, response.expiresInSeconds());
    }
}
