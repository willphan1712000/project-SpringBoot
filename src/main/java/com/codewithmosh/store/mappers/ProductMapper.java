package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.codewithmosh.store.dtos.product.ProductDto;
import com.codewithmosh.store.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    @Mapping( target = "category", ignore = true)
    @Mapping( target = "cartItems", ignore = true)
    @Mapping( target = "orderItems", ignore = true)
    Product toEntity(ProductDto request);
    
    @Mapping( target = "category", ignore = true)
    @Mapping( target = "cartItems", ignore = true)
    @Mapping( target = "orderItems", ignore = true)
    @Mapping( target = "id", ignore = true)
    void update(ProductDto request, @MappingTarget Product product);
}
