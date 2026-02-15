package com.codewithmosh.store.dtos.checkout;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WebhookRequest {
    private Map<String, String> headers;
    private String payload;
}
