package com.codewithmosh.store.services;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Jwt {
    private final SecretKey secretKey;
    private final Claims claims;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.secretKey = secretKey;
        this.claims = claims;
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject()); // because email is stored in token payload subject
    }

    public String getRole() {
        return claims.get("role", String.class);
    }

    @Override
    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
