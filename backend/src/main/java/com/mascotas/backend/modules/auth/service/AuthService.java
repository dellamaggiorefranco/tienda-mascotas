package com.mascotas.backend.modules.auth.service;

import com.mascotas.backend.modules.auth.dto.LoginRequest;
import com.mascotas.backend.modules.auth.dto.LoginResponse;
import com.mascotas.backend.modules.auth.dto.RegisterRequest;
import com.mascotas.backend.modules.auth.dto.RegisterResponse;
import com.mascotas.backend.modules.auth.exception.InvalidCredentialsException;
import com.mascotas.backend.modules.users.entity.User;
import com.mascotas.backend.modules.users.entity.UserRole;
import com.mascotas.backend.modules.users.exception.UserNotFoundException;
import com.mascotas.backend.modules.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {
        String passwordHash = passwordEncoder.encode(request.password());
        User user = userService.create(request.email(), passwordHash, UserRole.CUSTOMER);

        log.info("Usuario registrado: {}", user.getEmail());

        return new RegisterResponse(user.getId(), user.getEmail(), user.getRole());
    }

    public LoginResponse login(LoginRequest request) {
        User user;
        try {
            user = userService.findByEmail(request.email());
        } catch (UserNotFoundException ex) {
            log.warn("Login fallido, email no encontrado: {}", request.email());
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.warn("Login fallido, password incorrecta: {}", request.email());
            throw new InvalidCredentialsException();
        }

        log.info("Login exitoso: {}", user.getEmail());

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole());
        return new LoginResponse(accessToken, "Bearer", jwtService.getAccessTokenExpirationSeconds());
    }
}