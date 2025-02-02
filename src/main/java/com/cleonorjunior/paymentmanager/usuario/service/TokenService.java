package com.cleonorjunior.paymentmanager.usuario.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TokenService {

    private final String issuer;

    private final int expirationTimeMinutes;

    private final Algorithm algorithm;

    private final String zoneId;

    public enum TokenType {
        ACCESS, REFRESH
    }

    public TokenService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expiration-time-minutes}") int expirationTimeMinutes,
            @Value("${jwt.zoneId}") String zoneId
    ) {
        this.issuer = issuer;
        this.expirationTimeMinutes = expirationTimeMinutes;
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.zoneId = zoneId;
    }

    public String generateAccessToken(String subject) {
        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withIssuedAt(creationDate())
                    .withExpiresAt(expirationDate())
                    .withClaim("type", TokenType.ACCESS.name())
                    .withSubject(subject)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new JWTCreationException("Erro ao gerar token.", exception);
        }
    }

    public String getSubjectFromAccessToken(String token) {
        return getSubjectFromToken(token, TokenType.ACCESS);
    }

    public String getSubjectFromRefreshToken(String token) {
        return getSubjectFromToken(token, TokenType.REFRESH);
    }

    public String getSubjectFromToken(String token, TokenType type) {
        try {
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withClaim("type", type.name())
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }

    public String generateRefreshToken(String  subject) {
        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .withClaim("type", TokenType.REFRESH.name())
                    .withExpiresAt(
                            now().plusDays(7).toInstant()
                    )
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Erro ao gerar  refresh token", exception);
        }
    }



    private ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(zoneId));
    }

    private Instant creationDate() {
        return now().toInstant();
    }

    private Instant expirationDate() {
        return now().plusMinutes(expirationTimeMinutes).toInstant();
    }

}