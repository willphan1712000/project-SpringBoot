package com.codewithmosh.store.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.order.CheckoutCartDto;
import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.mappers.order.OrderMapper;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.CartService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CheckoutController {
    private final CartsRepository cartsRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    private final OrderMapper orderMapper;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutCartDto request) {
        var cartId = request.getCartId();
        var cart = cartsRepository.findById(cartId).orElse(null);
        if(cart == null) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "cart not found")
            );
        }

        if(cart.getCartItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "cart is empty")
            );
        }

        // Get current user
        var user = authService.getCurrentUser();

        // Get all items from the cart and put them into an order
        var order = Order.createOrderFrom(cart, user);

        // Save order
        orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(cartId);

        return ResponseEntity.ok(Map.of("orderId", order.getId()));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        // Get user id
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = (Long) authentication.getPrincipal();

        var orders = orderRepository.findByCustomerId(id);
        if(orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "order not found")
            );
        }

        return ResponseEntity.ok(orders.stream().map(orderMapper::toDto));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        // Get user id
        var user = authService.getCurrentUser();

        var order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error","order not found")
            );
        }

        if(user.getId() != order.getCustomer().getId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("error", "This order does not belong to you")
            );
        }

        return ResponseEntity.ok(orderMapper.toDto(order));
    }
}
