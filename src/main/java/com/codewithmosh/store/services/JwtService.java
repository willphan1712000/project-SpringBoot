package com.codewithmosh.store.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    public String generate(String email) {
        int expiration = 60 * 60 * 24;

        return Jwts
            .builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .signWith(Keys.hmacShaKeyFor("sdfw039rjkgjkdjg34t7345o23kfdsf".getBytes()))
            .compact();
    }
}
