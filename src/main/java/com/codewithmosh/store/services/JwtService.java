package com.codewithmosh.store.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generate(String email) {
        int expiration = 60 * 60 * 24;
        System.out.println(secret);

        return Jwts
            .builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
    }

    public String generateAccessToken(User user) {
        int expiration = 5 * 60 * 24; // 5 minutes
        return generateToken(user, expiration);
    }

    public String generateRefreshToken(User user) {
        int expiration = 7 * 24 * 60 * 60; // 7 days
        return generateToken(user, expiration);
    }

    private String generateToken(User user, Integer expiration) {
        return Jwts
            .builder()
            .subject(user.getId().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .claim("name", user.getName())
            .claim("email", user.getEmail())
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
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
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject(); // because email is stored in token payload subject
    }
}
