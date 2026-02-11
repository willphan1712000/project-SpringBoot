package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithmosh.store.dtos.cartItems.CartItemDto;
import com.codewithmosh.store.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping( target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
