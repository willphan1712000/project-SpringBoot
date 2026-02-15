package com.codewithmosh.store.services.external;

import com.codewithmosh.store.entities.orders.Order;

public interface PaymentService {
      CheckoutSession createCheckoutSession(Order order);
}
