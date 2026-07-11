package com.mascotas.backend.modules.auth.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Token invalido o expirado.");
    }
}