package com.codewithmosh.store.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig config;

    public String generate(String email) {
        int expiration = config.getAccessTokenExpiration();

        return Jwts
            .builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .signWith(config.getSecretKey())
            .compact();
    }

    public String generateAccessToken(User user) {
        return generateToken(user, config.getAccessTokenExpiration());
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, config.getRefreshTokenExpiration());
    }

    private String generateToken(User user, Integer expiration) {
        return Jwts
            .builder()
            .subject(user.getId().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .claim("name", user.getName())
            .claim("email", user.getEmail())
            .claim("role", user.getRole())
            .signWith(config.getSecretKey())
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(config.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubjectFrom(String token) {
        return getClaims(token).getSubject(); // because email is stored in token payload subject
    }

    public String getRoleFrom(String token) {
        return getClaims(token).get("role", String.class);
    }
}
