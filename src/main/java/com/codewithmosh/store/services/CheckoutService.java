package com.codewithmosh.store.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.dtos.order.FetchOrderDto;
import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.OrderNotBelongToUserException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.order.OrderMapper;
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

    private final OrderMapper orderMapper;

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

    public List<FetchOrderDto> getOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getAllByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public FetchOrderDto getOrder(Long orderId) {
        var user = authService.getCurrentUser();

        var order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            throw new OrderNotFoundException();
        }

        if(user.getId() != order.getCustomer().getId()) {
            throw new OrderNotBelongToUserException();
        }

        return orderMapper.toDto(order);
    }
}
