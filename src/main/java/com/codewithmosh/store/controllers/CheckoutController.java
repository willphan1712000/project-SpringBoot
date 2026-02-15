package com.codewithmosh.store.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.dtos.checkout.CheckoutCartDto;
import com.codewithmosh.store.dtos.checkout.WebhookRequest;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.services.CheckoutService;
import com.codewithmosh.store.services.external.PaymentException;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutCartDto request) {
        return ResponseEntity.ok(checkoutService.checkout(request.getCartId()));
    }

    @PostMapping("/webhook")
    public void webhook(
        @RequestHeader Map<String, String> headers,
        @RequestBody String payload
    ) {
        checkoutService.parseWebhook(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler({ CartEmptyException.class, CartNotFoundException.class })
    public ResponseEntity<ErrorDto> cartEmptyHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorDto(e.getMessage())
        );
    }

    @ExceptionHandler( PaymentException.class )
    public ResponseEntity<ErrorDto> paymentServiceExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new ErrorDto(e.getMessage() == null ? "Error creating a checkout session." : e.getMessage())
        );
    }
}
