package com.codewithmosh.store.mappers.order;

import org.mapstruct.Mapper;

import com.codewithmosh.store.dtos.cartItems.CartItemDto;
import com.codewithmosh.store.entities.orders.OrderItem;

/**
 * Map between CartItemDto and OrderItem
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    CartItemDto toDto(OrderItem orderItem);
}
