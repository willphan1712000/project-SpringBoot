package com.codewithmosh.store.dtos.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.codewithmosh.store.dtos.cartItems.CartItemDto;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    private LocalDateTime dateCreated;
    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
