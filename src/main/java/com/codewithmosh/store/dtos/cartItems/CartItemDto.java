package com.codewithmosh.store.dtos.cartItems;

import java.math.BigDecimal;

import com.codewithmosh.store.dtos.product.CartProductDto;

import lombok.Data;

@Data
public class CartItemDto {
    private CartProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
