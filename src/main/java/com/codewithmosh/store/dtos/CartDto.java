package com.codewithmosh.store.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.codewithmosh.store.entities.CartItem;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    private LocalDateTime dateCreated;
    private List<CartItem> cartItems = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
