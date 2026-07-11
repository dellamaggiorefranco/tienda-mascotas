package com.mascotas.backend.modules.auth.dto;

public record LoginResponse(String accessToken, String refreshToken, String tokenType, long expiresInSeconds) {
}