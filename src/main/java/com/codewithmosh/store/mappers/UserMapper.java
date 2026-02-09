package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.codewithmosh.store.dtos.RegisterUserDto;
import com.codewithmosh.store.dtos.UpdateUserDto;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserDto request);
    void update(UpdateUserDto request, @MappingTarget User user);
}
