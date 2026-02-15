package com.codewithmosh.store.dtos.checkout;

import com.codewithmosh.store.entities.orders.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResult {
    private Long orderId;
    private OrderStatus status;
}
