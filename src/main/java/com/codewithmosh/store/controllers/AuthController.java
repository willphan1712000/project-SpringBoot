package com.codewithmosh.store.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.dtos.JwtResponse;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.dtos.auth.SigninDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody SigninDto request, HttpServletResponse response) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        var accessToken = jwtService.generateAccessToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
        cookie.setSecure(true);
        response.addCookie(cookie);
        
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue( value = "refreshToken" ) String refreshToken) {
        var jwt = jwtService.parse(refreshToken);
        if(jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = (String) authentication.getPrincipal();

        var user = userRepository.findById(Long.parseLong(id)).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> badCredentialsExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
    }
}
