package com.mascotas.backend.modules.auth.dto;

public record RefreshResponse(String accessToken, String tokenType, long expiresInSeconds) {
}