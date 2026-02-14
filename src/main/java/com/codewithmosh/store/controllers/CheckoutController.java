package com.codewithmosh.store.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.cart.CheckoutCartDto;
import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.entities.orders.OrderStatus;
import com.codewithmosh.store.mappers.order.CartOrderMapper;
import com.codewithmosh.store.mappers.order.OrderMapper;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.CartService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CheckoutController {
    private final CartsRepository cartsRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    private final OrderMapper orderMapper;
    private final CartOrderMapper cartOrderMapper;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutCartDto request) {
        var cartId = request.getCartId();
        var cart = cartsRepository.findById(cartId).orElse(null);
        if(cart == null) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "cart not found")
            );
        }

        var cartItems = cart.getCartItems();
        if(cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "cart is empty")
            );
        }

        // Get user id
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = (Long) authentication.getPrincipal();

        // Get all items from the cart and put them into an order
        var order = new Order();
        order.setCustomerId(id);
        order.setOrderStatus(OrderStatus.PENDING);
        cartItems.stream().map(cartOrderMapper::toOrderItemFrom).forEach(order::addItem);
        order.setTotalPrice(order.getTotalPrice());

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
}
