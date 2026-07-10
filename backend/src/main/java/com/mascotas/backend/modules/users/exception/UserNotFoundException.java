package com.mascotas.backend.modules.users.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super("No se encontro un usuario con el email: " + email);
    }
}