package com.mascotas.backend.modules.auth.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds) {
}