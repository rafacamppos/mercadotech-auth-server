package com.mercadotech.authserver.application.useCase;

import com.mercadotech.authserver.domain.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TokenUseCaseImplTest {

    private TokenService tokenService;
    private TokenUseCase useCase;

    @BeforeEach
    void setUp() {
        tokenService = Mockito.mock(TokenService.class);
        useCase = new TokenUseCaseImpl(tokenService);
    }

    @Test
    void generateTokenDelegatesToService() {
        when(tokenService.generateToken("id", "secret")).thenReturn("token");
        String result = useCase.generateToken("id", "secret");
        assertThat(result).isEqualTo("token");
        verify(tokenService).generateToken("id", "secret");
    }

    @Test
    void validateTokenDelegatesToService() {
        when(tokenService.validateToken("tok", "sec")).thenReturn(true);
        boolean result = useCase.validateToken("tok", "sec");
        assertThat(result).isTrue();
        verify(tokenService).validateToken("tok", "sec");
    }
}
