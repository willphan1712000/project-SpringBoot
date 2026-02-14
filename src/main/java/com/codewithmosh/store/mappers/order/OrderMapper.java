package com.codewithmosh.store.mappers.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithmosh.store.dtos.order.FetchOrderDto;
import com.codewithmosh.store.entities.orders.Order;

/**
 * Map between FetchOrderDto and Order
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping( target = "items", source = "orderItems")
    @Mapping( target = "totalPrice", expression = "java(order.getTotalPrice())")
    @Mapping( target = "status", source = "orderStatus")
    FetchOrderDto toDto(Order order);
}
