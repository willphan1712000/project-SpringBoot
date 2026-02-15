package com.codewithmosh.store.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.config.StripeConfig;
import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.dtos.order.CheckoutCartDto;
import com.codewithmosh.store.entities.orders.OrderStatus;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.CheckoutService;
import com.codewithmosh.store.services.external.PaymentException;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final StripeConfig stripeConfig;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutCartDto request) {
        return ResponseEntity.ok(checkoutService.checkout(request.getCartId()));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
        @RequestHeader("Stripe-Signature") String signature,
        @RequestBody String payload
    ) {
        try {
            var event = Webhook.constructEvent(payload, signature, stripeConfig.getWebhookSecretKey());
            System.out.println(event.getApiVersion());
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

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    var paymentIntent = (PaymentIntent) stripeObject;
                    if(paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }

                case "payment_intent.failed" -> {
                    break;
                }

                default -> {
                    break;
                }
            }

            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler({ CartEmptyException.class, CartNotFoundException.class })
    public ResponseEntity<ErrorDto> cartEmptyHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorDto(e.getMessage())
        );
    }

    @ExceptionHandler( PaymentException.class )
    public ResponseEntity<ErrorDto> paymentServiceExceptionHandler() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new ErrorDto("Error creating a checkout session.")
        );
    }
}
