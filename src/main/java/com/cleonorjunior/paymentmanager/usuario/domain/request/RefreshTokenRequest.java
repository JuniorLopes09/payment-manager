package com.cleonorjunior.paymentmanager.usuario.domain.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank String refreshToken
) {
}