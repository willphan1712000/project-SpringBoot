package com.codewithmosh.store.services.external;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.config.StripeConfig;
import com.codewithmosh.store.dtos.checkout.PaymentResult;
import com.codewithmosh.store.dtos.checkout.WebhookRequest;
import com.codewithmosh.store.entities.orders.Order;
import com.codewithmosh.store.entities.orders.OrderItem;
import com.codewithmosh.store.entities.orders.OrderStatus;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripePaymentService implements PaymentService {
    @Value("${url}")
    private String url;

    private final StripeConfig stripeConfig;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            // Create a checkout session
            var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(url + "/success.html?orderId=" + order.getId())
                .setCancelUrl(url + "/cancel.html")
                .setPaymentIntentData(createPaymentIntent(order));
    
            order.getOrderItems().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });
    
            var session = Session.create(builder.build());

            return new CheckoutSession(session.getUrl());
        } catch (StripeException ex) {
            System.out.println(ex); // simulate logging service
            throw new PaymentException();
        }
    }

    private SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder()
            .putMetadata("order_id", order.getId().toString()).build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(item.getProduct().getName()).build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                    .setProductData(
                        createProductData(item)
                    ).build();
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(
                    createPriceData(item)
                ).build();
    }

    @Override
    public Optional<PaymentResult> parseWebhook(WebhookRequest request) {
        try {
            var event = Webhook.constructEvent(request.getPayload(), request.getHeaders().get("Stripe-Signature"), stripeConfig.getWebhookSecretKey());

            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                    Optional.of(new PaymentResult(getOrderIdFromEvent(event), OrderStatus.PAID));

                case "payment_intent.payment_failed" ->
                    Optional.of(new PaymentResult(getOrderIdFromEvent(event), OrderStatus.FAILED));

                default ->
                    Optional.empty();
            };
        } catch (SignatureVerificationException e) {
            System.out.println(e.getMessage()); // simulate logging service
            throw new PaymentException("Invalid Signature");
        }
    }

    private Long getOrderIdFromEvent(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if(dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            try {
                stripeObject = dataObjectDeserializer.deserializeUnsafe();
            } catch (EventDataObjectDeserializationException e) {
                System.out.println(e.getMessage());
            }
        }

        var paymentIntent = (PaymentIntent) stripeObject;
        if(paymentIntent == null) {
            throw new PaymentException("Payment Intent null");
        }
        
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }
}
