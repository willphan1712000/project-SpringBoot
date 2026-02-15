package com.codewithmosh.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Configuration
@ConfigurationProperties( prefix = "stripe" )
@Data
public class StripeConfig {
    private String secret;
    private String webhookSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secret;
    }
}
