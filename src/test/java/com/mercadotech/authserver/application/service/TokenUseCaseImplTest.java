package com.mercadotech.authserver.application.service;

import com.mercadotech.authserver.application.useCase.TokenUseCase;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import com.mercadotech.authserver.application.useCase.TokenUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TokenUseCaseImplTest {

    private TokenService tokenService;
    private TokenCacheService cacheService;
    private TokenUseCase useCase;

    @BeforeEach
    void setUp() {
        tokenService = Mockito.mock(TokenService.class);
        cacheService = Mockito.mock(TokenCacheService.class);
        useCase = new TokenUseCaseImpl(tokenService, cacheService);
    }

    @Test
    void generateTokenGeneratesWhenCacheMiss() {
        Credentials credentials = Credentials.builder()
                .clientId("id")
                .clientSecret("secret")
                .build();
        TokenData tokenData = TokenData.builder().token("token").build();

        when(cacheService.get("id")).thenReturn(null);
        when(tokenService.generateToken(credentials)).thenReturn(tokenData);

        TokenData result = useCase.generateToken(credentials);

        assertThat(result).isEqualTo(tokenData);
        verify(tokenService).generateToken(credentials);
        verify(cacheService).save("id", "token");
    }

    @Test
    void generateTokenReturnsCachedTokenWhenAvailable() {
        Credentials credentials = Credentials.builder()
                .clientId("id")
                .clientSecret("secret")
                .build();

        when(cacheService.get("id")).thenReturn("cached");

        TokenData result = useCase.generateToken(credentials);

        assertThat(result).isEqualTo(TokenData.builder().token("cached").build());
        verifyNoInteractions(tokenService);
        verify(cacheService, never()).save(anyString(), anyString());
    }

    @Test
    void validateTokenDelegatesToService() {
        Credentials credentials = Credentials.builder()
                .clientSecret("sec")
                .build();
        TokenData tokenData = TokenData.builder().token("tok").build();

        when(tokenService.validateToken(tokenData, credentials)).thenReturn(true);

        boolean result = useCase.validateToken(tokenData, credentials);

        assertThat(result).isTrue();
        verify(tokenService).validateToken(tokenData, credentials);
    }
}
