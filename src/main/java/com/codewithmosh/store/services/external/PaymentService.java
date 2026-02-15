package com.codewithmosh.store.services.external;

import java.util.Optional;

import com.codewithmosh.store.dtos.checkout.PaymentResult;
import com.codewithmosh.store.dtos.checkout.WebhookRequest;
import com.codewithmosh.store.entities.orders.Order;

public interface PaymentService {
      CheckoutSession createCheckoutSession(Order order);
      Optional<PaymentResult> parseWebhook(WebhookRequest request);
}
