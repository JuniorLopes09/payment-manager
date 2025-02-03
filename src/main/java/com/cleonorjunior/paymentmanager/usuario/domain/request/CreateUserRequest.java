package com.cleonorjunior.paymentmanager.usuario.domain.request;


import com.cleonorjunior.paymentmanager.configuration.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @Email @NotBlank String email,
        @ValidPassword @NotBlank String senha
) {
}