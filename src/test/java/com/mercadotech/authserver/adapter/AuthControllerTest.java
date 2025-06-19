package com.mercadotech.authserver.adapter;

import com.mercadotech.authserver.adapter.dto.LoginRequest;
import com.mercadotech.authserver.adapter.dto.TokenResponse;
import com.mercadotech.authserver.adapter.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.dto.ValidateResponse;
import com.mercadotech.authserver.logging.StructuredLogger;
import com.mercadotech.authserver.logging.DefaultStructuredLogger;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import com.mercadotech.authserver.application.useCase.TokenUseCase;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private TokenUseCase useCase;
    private AuthController controller;
    private SimpleMeterRegistry registry;
    private Counter counter;
    private Timer loginTimer;
    private Timer validateTimer;
    private StructuredLogger logger;

    @BeforeEach
    void setUp() {
        useCase = Mockito.mock(TokenUseCase.class);
        registry = new SimpleMeterRegistry();
        counter = Counter.builder("auth_tokens_issued")
                .description("Number of tokens issued")
                .register(registry);
        loginTimer = Timer.builder("auth_login_latency")
                .description("Latency of login endpoint")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
        validateTimer = Timer.builder("auth_validate_latency")
                .description("Latency of token validation endpoint")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
        logger = new DefaultStructuredLogger(AuthController.class);
        controller = new AuthController(useCase, counter, loginTimer, validateTimer, logger);
    }

    @Test
    void loginReturnsTokenFromUseCase() {
        LoginRequest request = new LoginRequest();
        request.setClientId("id");
        request.setClientSecret("sec");
        Credentials credentials = Credentials.builder()
                .clientId("id")
                .clientSecret("sec")
                .build();
        TokenData tokenData = TokenData.builder().token("tok").build();
        when(useCase.generateToken(credentials)).thenReturn(tokenData);

        ResponseEntity<TokenResponse> response = controller.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo("tok");
        verify(useCase).generateToken(credentials);
    }

    @Test
    void validateReturnsResultFromUseCase() {
        ValidateRequest request = new ValidateRequest();
        request.setToken("tok");
        request.setClientSecret("sec");
        Credentials credentials = Credentials.builder()
                .clientSecret("sec")
                .build();
        TokenData tokenData = TokenData.builder().token("tok").build();
        when(useCase.validateToken(tokenData, credentials)).thenReturn(true);

        ResponseEntity<ValidateResponse> response = controller.validate(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isValid()).isTrue();
        verify(useCase).validateToken(tokenData, credentials);
    }

    @Test
    void loginReturnsUnauthorizedOnException() {
        LoginRequest request = new LoginRequest();
        request.setClientId("id");
        request.setClientSecret("sec");
        Credentials credentials = Credentials.builder()
                .clientId("id")
                .clientSecret("sec")
                .build();
        when(useCase.generateToken(credentials)).thenThrow(new RuntimeException("err"));

        ResponseEntity<TokenResponse> response = controller.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(useCase).generateToken(credentials);
    }
}
