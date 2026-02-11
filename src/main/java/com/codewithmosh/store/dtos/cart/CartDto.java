package com.codewithmosh.store.dtos.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.codewithmosh.store.dtos.cartItems.CartItemDto;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    private LocalDateTime dateCreated;
    private Set<CartItemDto> items = new HashSet<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
