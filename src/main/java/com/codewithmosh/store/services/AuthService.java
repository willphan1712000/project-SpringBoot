package com.codewithmosh.store.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = (Long) authentication.getPrincipal();

        return userRepository.findById(id).orElse(null);
    }
}
