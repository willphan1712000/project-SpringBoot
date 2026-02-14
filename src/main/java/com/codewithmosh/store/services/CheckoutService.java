package com.codewithmosh.store.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final CartsRepository cartsRepository;

    private final CartService cartService;
    private final AuthService authService;

    public Order checkout(UUID cartId) {
        var cart = cartsRepository.findById(cartId).orElse(null);
        if(cart == null) {
            throw new CartNotFoundException();
        }

        if(cart.getCartItems().isEmpty()) {
            throw new CartEmptyException();
        }

        // Get current user
        var user = authService.getCurrentUser();

        // Get all items from the cart and put them into an order
        var order = Order.createOrderFrom(cart, user);

        // Save order
        orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(cartId);

        return order;
    }
}
