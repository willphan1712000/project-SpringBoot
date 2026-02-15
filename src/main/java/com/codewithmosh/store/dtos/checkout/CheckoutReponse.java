package com.codewithmosh.store.dtos.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutReponse {
    private Long orderId;
    private String url;
}
