package com.codewithmosh.store.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codewithmosh.store.dtos.order.CheckoutReponse;
import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.external.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final CartsRepository cartsRepository;

    private final CartService cartService;
    private final AuthService authService;

    private final PaymentService paymentService;

    @Transactional
    public CheckoutReponse checkout(UUID cartId) {
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

        // Get checkout session
        var session = paymentService.createCheckoutSession(order);

        // Save order
        orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(cartId);

        return new CheckoutReponse(order.getId(), session.getCheckoutUrl());
    }
}
