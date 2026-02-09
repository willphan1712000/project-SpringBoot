package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class ChangePasswordUserDto {
    private String oldPassword;
    private String newPassword;
}
