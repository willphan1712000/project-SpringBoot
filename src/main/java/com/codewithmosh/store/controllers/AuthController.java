package com.codewithmosh.store.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.JwtResponse;
import com.codewithmosh.store.dtos.auth.SigninDto;
import com.codewithmosh.store.services.JwtService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody SigninDto request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var token = jwtService.generate(request.getEmail());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> badCredentialsExceptionHandler() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
