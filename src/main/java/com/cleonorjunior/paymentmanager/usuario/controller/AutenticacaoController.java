package com.cleonorjunior.paymentmanager.usuario.controller;

import com.cleonorjunior.paymentmanager.usuario.domain.request.CreateUserRequest;
import com.cleonorjunior.paymentmanager.usuario.domain.request.LoginRequest;
import com.cleonorjunior.paymentmanager.usuario.domain.request.RefreshTokenRequest;
import com.cleonorjunior.paymentmanager.usuario.domain.response.CreateUserResponse;
import com.cleonorjunior.paymentmanager.usuario.domain.response.LoginResponse;
import com.cleonorjunior.paymentmanager.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(AutenticacaoController.BASE_PATH)
@Tag(name = "Autenticação")
public class AutenticacaoController {

    public static final String BASE_PATH = "/auth";

    private final UsuarioService usuarioService;

    public AutenticacaoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/login")
    @Operation(description = "Realizar login na aplicação")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginUserDto) {
        LoginResponse token = usuarioService.authenticateUser(loginUserDto);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    @Operation(description = "Registrar novo usuário")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest, UriComponentsBuilder uriBuilder) {

        CreateUserResponse response = usuarioService.createUser(createUserRequest);

        URI uri = uriBuilder.path(BASE_PATH + "/login").build().toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/refresh")
    @Operation(description = "Atualizar token de autenticação")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest refreshToken) {
        LoginResponse token = usuarioService.refreshJwtToken(refreshToken.refreshToken());

        return ResponseEntity.ok(token);
    }
}
