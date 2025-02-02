package com.cleonorjunior.paymentmanager.usuario.domain.request;

public record LoginRequest(
        String email,
        String senha
) {
}