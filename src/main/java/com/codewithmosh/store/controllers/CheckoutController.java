package com.codewithmosh.store.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.dtos.order.CheckoutCartDto;
import com.codewithmosh.store.dtos.order.FetchOrderDto;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.OrderNotBelongToUserException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.services.CheckoutService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutCartDto request) {
        var order = checkoutService.checkout(request.getCartId());
        return ResponseEntity.ok(Map.of("orderId", order.getId()));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<FetchOrderDto>> getOrders() {
        return ResponseEntity.ok(checkoutService.getOrders());
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<FetchOrderDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(checkoutService.getOrder(orderId));
    }

    @ExceptionHandler({ CartEmptyException.class, CartNotFoundException.class, OrderNotBelongToUserException.class })
    public ResponseEntity<ErrorDto> cartEmptyHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorDto(e.getMessage())
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> orderNotFoundHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErrorDto(e.getMessage())
        );
    }
}
