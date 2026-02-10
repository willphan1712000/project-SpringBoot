package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.entities.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", ignore = true)
    CartDto toDto(Cart cart);
}
