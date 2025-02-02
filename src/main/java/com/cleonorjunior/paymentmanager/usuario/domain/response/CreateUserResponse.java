package com.cleonorjunior.paymentmanager.usuario.domain.response;


import com.cleonorjunior.paymentmanager.usuario.domain.model.Cargo;

import java.util.List;

public record CreateUserResponse(
        String email,
        List<Cargo> cargos
) {
}