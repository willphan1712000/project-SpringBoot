package com.codewithmosh.store.mappers.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.orders.OrderItem;

/**
 * Map between CartItem and OrderItem
 */
@Mapper(componentModel = "spring")
public interface CartOrderMapper {
    @Mapping( target = "id", ignore = true )
    @Mapping( target = "order", ignore = true )
    @Mapping( target = "unitPrice", source = "product.price")
    @Mapping( target = "totalPrice", expression = "java(cartItem.getTotalPrice())" )
    OrderItem toOrderItemFrom(CartItem cartItem);
}
