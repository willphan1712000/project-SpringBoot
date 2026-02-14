package com.codewithmosh.store.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.dtos.order.FetchOrderDto;
import com.codewithmosh.store.exceptions.OrderNotBelongToUserException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.services.OrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    
    @GetMapping
    public ResponseEntity<List<FetchOrderDto>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FetchOrderDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @ExceptionHandler({ OrderNotBelongToUserException.class })
    public ResponseEntity<ErrorDto> cartEmptyHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
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
