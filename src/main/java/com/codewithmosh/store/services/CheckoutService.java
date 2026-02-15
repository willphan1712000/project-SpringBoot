package com.codewithmosh.store.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.dtos.order.CheckoutReponse;
import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    @Value("")
    private String url;

    private final OrderRepository orderRepository;
    private final CartsRepository cartsRepository;

    private final CartService cartService;
    private final AuthService authService;

    public CheckoutReponse checkout(UUID cartId) throws StripeException {
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

        // Create a checkout session
        var builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(url + "success.html?orderId" + order.getId())
            .setCancelUrl(url + "cancel.html");

        order.getOrderItems().forEach(item -> {
            var lineItem = SessionCreateParams.LineItem.builder()
            .setQuantity(Long.valueOf(item.getQuantity()))
            .setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(item.getUnitPrice())
                .setProductData(
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(item.getProduct().getName()).build()
                ).build()
            ).build();

            builder.addLineItem(lineItem);
        });

        var session = Session .create(builder.build());

        // Clear the cart
        cartService.clearCart(cartId);

        return new CheckoutReponse(order.getId(), session.getUrl());
    }
}
