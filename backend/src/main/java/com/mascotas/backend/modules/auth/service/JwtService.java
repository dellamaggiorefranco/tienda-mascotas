package com.mascotas.backend.modules.auth.service;

import com.mascotas.backend.config.JwtProperties;
import com.mascotas.backend.modules.auth.exception.InvalidTokenException;
import com.mascotas.backend.modules.users.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    private final SecretKey key;
    private final long expirationMinutes;
    private final long refreshExpirationDays;

    //clave para firmar
    public JwtService(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = jwtProperties.expirationMinutes();
        this.refreshExpirationDays = jwtProperties.refreshExpirationDays();
    }

    //generar el token
    public String generateAccessToken(String email, UserRole role) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationMinutes * 60);
        //claims y firma
        return Jwts.builder()
                .subject(email)
                .claim(CLAIM_ROLE, role.name())
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    //validar el token
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException();
        }
    }

    public String generateRefreshToken(String email) {
        Instant now = Instant.now();
        Instant expiration = now.plus(Duration.ofDays(refreshExpirationDays));

        return Jwts.builder()
                .subject(email)
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    public String extractEmail(Claims claims) {
        return claims.getSubject();
    }

    public UserRole extractRole(Claims claims) {
        return UserRole.valueOf(claims.get(CLAIM_ROLE, String.class));
    }

    public boolean isRefreshToken(Claims claims) {
        return TYPE_REFRESH.equals(claims.get(CLAIM_TYPE, String.class));
    }

    public long getAccessTokenExpirationSeconds() {
        return expirationMinutes * 60;
    }
}