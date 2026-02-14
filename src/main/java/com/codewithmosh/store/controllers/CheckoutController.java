package com.codewithmosh.store.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.dtos.order.CheckoutCartDto;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.services.CheckoutService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutCartDto request) {
        var order = checkoutService.checkout(request.getCartId());
        return ResponseEntity.ok(Map.of("orderId", order.getId()));
    }

    @ExceptionHandler({ CartEmptyException.class, CartNotFoundException.class })
    public ResponseEntity<ErrorDto> cartEmptyHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorDto(e.getMessage())
        );
    }
}
