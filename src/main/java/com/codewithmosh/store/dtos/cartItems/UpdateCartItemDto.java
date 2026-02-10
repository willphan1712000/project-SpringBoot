package com.codewithmosh.store.dtos.cartItems;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @Min(value = 1, message="quantity must be between 1 and 100")
    @Max(value = 100, message="quantity must be between 1 and 100")
    private Integer quantity;
}
