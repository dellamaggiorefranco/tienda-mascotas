package com.mascotas.backend.modules.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mascotas.backend.config.JwtProperties;
import com.mascotas.backend.modules.auth.exception.InvalidTokenException;
import com.mascotas.backend.modules.users.entity.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String TEST_SECRET = "un-secreto-de-prueba-bien-largo-para-hs256-1234567890";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(new JwtProperties(TEST_SECRET, 15, 7));
    }

    @Test
    void generateAndParseAccessToken() {
        String token = jwtService.generateAccessToken("juan@example.com", UserRole.CUSTOMER);

        Claims claims = jwtService.parseToken(token);

        assertEquals("juan@example.com", jwtService.extractEmail(claims));
        assertEquals(UserRole.CUSTOMER, jwtService.extractRole(claims));
        assertEquals("access", claims.get("type", String.class));
    }

    @Test
    void expiredTokenThrowsInvalidTokenException() {
        JwtService expiredJwtService = new JwtService(new JwtProperties(TEST_SECRET, -1, 7));
        String token = expiredJwtService.generateAccessToken("juan@example.com", UserRole.CUSTOMER);

        assertThrows(InvalidTokenException.class, () -> expiredJwtService.parseToken(token));
    }

    @Test
    void tamperedSignatureThrowsInvalidTokenException() {
        String token = jwtService.generateAccessToken("juan@example.com", UserRole.CUSTOMER);
        String tampered = token.substring(0, token.length() - 1) + (token.endsWith("A") ? "B" : "A");

        assertThrows(InvalidTokenException.class, () -> jwtService.parseToken(tampered));
    }

    @Test
    void generateAndParseRefreshToken() {
        String token = jwtService.generateRefreshToken("juan@example.com");

        Claims claims = jwtService.parseToken(token);

        assertEquals("juan@example.com", jwtService.extractEmail(claims));
        assertEquals(true, jwtService.isRefreshToken(claims));
    }

    @Test
    void isRefreshTokenIsFalseForAccessToken() {
        String token = jwtService.generateAccessToken("juan@example.com", UserRole.CUSTOMER);

        Claims claims = jwtService.parseToken(token);

        assertEquals(false, jwtService.isRefreshToken(claims));
    }
}