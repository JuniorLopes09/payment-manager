package com.cleonorjunior.paymentmanager.usuario.service;

import com.cleonorjunior.paymentmanager.usuario.domain.enums.TipoCargo;
import com.cleonorjunior.paymentmanager.usuario.domain.model.Cargo;
import com.cleonorjunior.paymentmanager.usuario.domain.model.Usuario;
import com.cleonorjunior.paymentmanager.usuario.domain.request.CreateUserRequest;
import com.cleonorjunior.paymentmanager.usuario.domain.request.LoginRequest;
import com.cleonorjunior.paymentmanager.usuario.domain.response.CreateUserResponse;
import com.cleonorjunior.paymentmanager.usuario.domain.response.LoginResponse;
import com.cleonorjunior.paymentmanager.usuario.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioService {

    private final AuthenticationManager authenticationManager;

    private final TokenService jwtTokenService;

    private final UsuarioRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(AuthenticationManager authenticationManager, TokenService jwtTokenService, UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse authenticateUser(LoginRequest loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.senha());


        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        Usuario usuario = (Usuario) authentication.getPrincipal();
        String subject = usuario.getEmail();

        return new LoginResponse(jwtTokenService.generateAccessToken(subject), jwtTokenService.generateRefreshToken(subject));
    }

    public LoginResponse refreshJwtToken(String refreshToken) {
        String email = jwtTokenService.getSubjectFromRefreshToken(refreshToken);

        return new LoginResponse(
                jwtTokenService.generateAccessToken(email),
                jwtTokenService.generateRefreshToken(email)
        );
    }


    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {

        Usuario newUser = Usuario.builder()
                .email(createUserRequest.email())
                .senha(passwordEncoder.encode(createUserRequest.senha()))
                .cargos(
                        Collections.singletonList(
                                Cargo.builder().cargo(TipoCargo.USUARIO).build()
                        )
                )
                .build();

        Usuario persistedUser = userRepository.save(newUser);

        return new CreateUserResponse(
                persistedUser.getEmail(), persistedUser.getCargos()
        );
    }
}