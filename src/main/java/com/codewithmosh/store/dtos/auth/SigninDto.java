package com.codewithmosh.store.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninDto {
    @NotBlank(message = "email must not be blank")
    @Email
    private String email;

    @NotBlank(message = "password must not be blank")
    private String password;
}