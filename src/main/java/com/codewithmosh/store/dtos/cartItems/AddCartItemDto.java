package com.codewithmosh.store.dtos.cartItems;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartItemDto {
    @NotNull(message = "product id must not be null")
    private Long productId;
}
