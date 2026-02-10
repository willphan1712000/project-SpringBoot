package com.codewithmosh.store.dtos.cartItems;

import lombok.Data;

@Data
public class AddCartItemDto {
    private Long productId;
    private Integer quantity;
}
