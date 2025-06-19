package com.mercadotech.authserver.adapter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;

public class JwtTokenServiceTest {

    private final JwtTokenService service = new JwtTokenService();
    private final String clientId = "client";
    private final String secret = "secretsecretsecretsecretsecretsecret"; // 32+ characters for HS256

    @Test
    void generateTokenReturnsValidJwt() {
        Credentials credentials = Credentials.builder()
                .clientId(clientId)
                .clientSecret(secret)
                .build();
        TokenData token = service.generateToken(credentials);
        assertThat(token.getToken()).isNotNull();
        assertThat(service.validateToken(token, credentials)).isTrue();
    }

    @Test
    void validateTokenFailsWithWrongSecret() {
        Credentials credentials = Credentials.builder()
                .clientId(clientId)
                .clientSecret(secret)
                .build();
        TokenData token = service.generateToken(credentials);
        Credentials wrongCredentials = Credentials.builder()
                .clientSecret(secret + "x")
                .build();
        assertThat(service.validateToken(token, wrongCredentials)).isFalse();
    }
}
