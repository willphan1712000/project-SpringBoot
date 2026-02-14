package com.codewithmosh.store.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.cart.CheckoutCartDto;
import com.codewithmosh.store.dtos.order.OrderDto;
import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.entities.orders.OrderItem;
import com.codewithmosh.store.entities.orders.OrderStatus;
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
        cartItems.stream().forEach(item -> {
            var orderItem = new OrderItem();
            
            orderItem.setProductId(item.getProduct().getId());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getProduct().getPrice()); // tax 0%

            order.addItem(orderItem);
        });
        order.setTotalPrice(order.getTotalPrice());

        // Save order
        orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(cartId);

        return ResponseEntity.ok(new OrderDto(order.getId()));
    }

    @GetMapping("/orders")
    public ResponseEntity<Void> getOrders() {
        return null;
    }
}
