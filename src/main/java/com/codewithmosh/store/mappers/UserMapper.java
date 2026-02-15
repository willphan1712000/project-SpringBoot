package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.codewithmosh.store.dtos.RegisterUserDto;
import com.codewithmosh.store.dtos.UpdateUserDto;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping( target = "createdAt", ignore = true )
    UserDto toDto(User user);

    @Mapping( target = "id", ignore = true)
    @Mapping( target = "addresses", ignore = true)
    @Mapping( target = "profile", ignore = true)
    @Mapping( target = "favoriteProducts", ignore = true)
    @Mapping( target = "role", ignore = true)
    @Mapping( target = "orders", ignore = true)
    User toEntity(RegisterUserDto request);
    
    @Mapping( target = "addresses", ignore = true)
    @Mapping( target = "profile", ignore = true)
    @Mapping( target = "favoriteProducts", ignore = true)
    @Mapping( target = "id", ignore =  true)
    @Mapping( target = "password", ignore =  true)
    @Mapping( target = "role", ignore = true)
    @Mapping( target = "orders", ignore = true)
    void update(UpdateUserDto request, @MappingTarget User user);
}
