package com.mascotas.backend.modules.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mascotas.backend.AbstractIntegrationTest;
import com.mascotas.backend.modules.auth.dto.LoginRequest;
import com.mascotas.backend.modules.auth.dto.RegisterRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Disabled("Testcontainers no arranca con el Docker Desktop actual (Engine API 1.55, Moby v2) "
        + "- ver nota en docs/SPRINTS.md backlog. Reactivar cuando haya una version compatible.")
class AuthIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerReturnsCreated() throws Exception {
        RegisterRequest request = new RegisterRequest("nuevo@example.com", "password123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("nuevo@example.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    void registerWithDuplicateEmailReturnsConflict() throws Exception {
        RegisterRequest request = new RegisterRequest("duplicado@example.com", "password123");
        register(request);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("EMAIL_ALREADY_EXISTS"));
    }

    @Test
    void registerWithInvalidBodyReturnsBadRequest() throws Exception {
        String invalidJson = "{\"email\":\"no-es-un-email\",\"password\":\"123\"}";

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void loginWithCorrectCredentialsReturnsToken() throws Exception {
        register(new RegisterRequest("login-ok@example.com", "password123"));

        LoginRequest loginRequest = new LoginRequest("login-ok@example.com", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void loginWithUnknownEmailAndWrongPasswordReturnTheSameError() throws Exception {
        register(new RegisterRequest("existe@example.com", "password123"));

        String unknownEmailBody = objectMapper.writeValueAsString(
                new LoginRequest("no-existe@example.com", "password123"));
        String wrongPasswordBody = objectMapper.writeValueAsString(
                new LoginRequest("existe@example.com", "password-incorrecta"));

        String unknownEmailResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unknownEmailBody))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        String wrongPasswordResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongPasswordBody))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        assertEquals(extractField(unknownEmailResponse, "error"), extractField(wrongPasswordResponse, "error"));
        assertEquals(extractField(unknownEmailResponse, "message"), extractField(wrongPasswordResponse, "message"));
    }

    private void register(RegisterRequest request) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private String extractField(String json, String field) throws Exception {
        return objectMapper.readTree(json).get(field).asText();
    }
}