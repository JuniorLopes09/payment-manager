package com.cleonorjunior.paymentmanager.usuario.domain.response;

public record LoginResponse(
        String token,
        String refreshToken
) {
}