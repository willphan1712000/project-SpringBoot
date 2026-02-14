package com.codewithmosh.store.dtos.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.codewithmosh.store.dtos.cartItems.CartItemDto;

import lombok.Data;

@Data
public class FetchOrderDto {
    private Long id;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
}
