package com.codewithmosh.store.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.dtos.order.FetchOrderDto;
import com.codewithmosh.store.exceptions.OrderNotBelongToUserException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.order.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthService authService;

    private final OrderMapper orderMapper;

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

        if(!order.has(user)) {
            throw new OrderNotBelongToUserException();
        }

        return orderMapper.toDto(order);
    }
}
