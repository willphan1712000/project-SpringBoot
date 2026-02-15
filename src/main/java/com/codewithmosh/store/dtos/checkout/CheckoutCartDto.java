package com.codewithmosh.store.dtos.checkout;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutCartDto {
    @NotNull( message = "cart id must not be null")
    private UUID cartId;
}
