package com.mascotas.backend.common.response;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ErrorResponse(Instant timestamp, int status, String error, String message) {

    public static ErrorResponse of(HttpStatus status, String error, String message) {
        return new ErrorResponse(Instant.now(), status.value(), error, message);
    }
}