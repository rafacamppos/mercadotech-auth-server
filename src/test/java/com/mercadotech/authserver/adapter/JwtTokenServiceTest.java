package com.mercadotech.authserver.adapter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenServiceTest {

    private final JwtTokenService service = new JwtTokenService();
    private final String clientId = "client";
    private final String secret = "secretsecretsecretsecretsecretsecret"; // 32+ characters for HS256

    @Test
    void generateTokenReturnsValidJwt() {
        String token = service.generateToken(clientId, secret);
        assertThat(token).isNotNull();
        assertThat(service.validateToken(token, secret)).isTrue();
    }

    @Test
    void validateTokenFailsWithWrongSecret() {
        String token = service.generateToken(clientId, secret);
        assertThat(service.validateToken(token, secret + "x")).isFalse();
    }
}
