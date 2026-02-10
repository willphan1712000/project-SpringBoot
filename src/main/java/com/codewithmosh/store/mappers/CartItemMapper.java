package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithmosh.store.dtos.cartItems.AddCartItemDto;
import com.codewithmosh.store.dtos.cartItems.CartItemDto;
import com.codewithmosh.store.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "product.id", source = "productId" )
    CartItem toEntity(AddCartItemDto request);
}
