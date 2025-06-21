package com.mercadotech.authserver.adapter;

import com.mercadotech.authserver.application.service.JwtTokenService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import com.mercadotech.authserver.exception.BusinessException;

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
    void validateTokenThrowsBusinessExceptionWithWrongSecret() {
        Credentials credentials = Credentials.builder()
                .clientId(clientId)
                .clientSecret(secret)
                .build();
        TokenData token = service.generateToken(credentials);
        Credentials wrongCredentials = Credentials.builder()
                .clientSecret(secret + "x")
                .build();
        assertThatThrownBy(() -> service.validateToken(token, wrongCredentials))
                .isInstanceOf(BusinessException.class);
    }
}
