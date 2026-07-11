package com.mascotas.backend.modules.auth.dto;

import com.mascotas.backend.modules.users.entity.UserRole;

public record RegisterResponse(Long id, String email, UserRole role) {
}