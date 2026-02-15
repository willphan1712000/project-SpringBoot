package com.codewithmosh.store.services.external;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.entities.orders.Order;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripePaymentService implements PaymentService {
    @Value("${url}")
    private String url;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
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
                    .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(item.getProduct().getName()).build()
                    ).build()
                ).build();
    
                builder.addLineItem(lineItem);
            });
    
            var session = Session.create(builder.build());

            return new CheckoutSession(session.getUrl());
        } catch (StripeException ex) {
            System.out.println(ex); // simulate logging service
            throw new PaymentException();
        }
    }
    
}
