package com.codewithmosh.store.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.entities.User;

import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig config;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, config.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, config.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, Integer expiration) {
        var claims = Jwts.claims()
            .subject(user.getId().toString())
            .add("name", user.getName())
            .add("email", user.getEmail())
            .add("role", user.getRole())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .build();

        return new Jwt(claims, config.getSecretKey());
    }

    public Jwt parse(String token) {
        try {
            var claims = Jwts
                    .parser()
                    .verifyWith(config.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return new Jwt(claims, config.getSecretKey());
        } catch (Exception e) {
            return null;
        }
    }
}
