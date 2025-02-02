package com.cleonorjunior.paymentmanager.usuario.domain.request;


public record CreateUserRequest(
        String email,
        String senha
) {
}