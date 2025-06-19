package com.mercadotech.authserver.adapter;

import com.mercadotech.authserver.adapter.server.dto.LoginRequest;
import com.mercadotech.authserver.adapter.server.dto.TokenResponse;
import com.mercadotech.authserver.adapter.server.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.server.dto.ValidateResponse;
import com.mercadotech.authserver.adapter.server.controller.AuthController;
import com.mercadotech.authserver.adapter.database.repository.CredentialsRepository;
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
    private CredentialsRepository repository;
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
        repository = Mockito.mock(CredentialsRepository.class);
        logger = new DefaultStructuredLogger(AuthController.class);
        controller = new AuthController(useCase, counter, loginTimer, validateTimer, repository);
    }

    @Test
    void loginReturnsTokenFromUseCase() {
        LoginRequest request = new LoginRequest();
        request.setClientId("550e8400-e29b-41d4-a716-446655440000");
        request.setClientSecret("123e4567-e89b-12d3-a456-426614174000");
        Credentials credentials = Credentials.builder()
                .clientId("550e8400-e29b-41d4-a716-446655440000")
                .clientSecret("123e4567-e89b-12d3-a456-426614174000")
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
        request.setClientSecret("123e4567-e89b-12d3-a456-426614174000");
        Credentials credentials = Credentials.builder()
                .clientSecret("123e4567-e89b-12d3-a456-426614174000")
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
        request.setClientId("550e8400-e29b-41d4-a716-446655440000");
        request.setClientSecret("123e4567-e89b-12d3-a456-426614174000");
        Credentials credentials = Credentials.builder()
                .clientId("550e8400-e29b-41d4-a716-446655440000")
                .clientSecret("123e4567-e89b-12d3-a456-426614174000")
                .build();
        when(useCase.generateToken(credentials)).thenThrow(new RuntimeException("err"));

        ResponseEntity<TokenResponse> response = controller.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(useCase).generateToken(credentials);
    }

    @Test
    void loginReturnsUnauthorizedWhenCredentialsAreNotUuid() {
        LoginRequest request = new LoginRequest();
        request.setClientId("not-uuid");
        request.setClientSecret("also-not-uuid");

        ResponseEntity<TokenResponse> response = controller.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verifyNoInteractions(useCase);
    }

    @Test
    void validateReturnsUnauthorizedWhenSecretNotUuid() {
        ValidateRequest request = new ValidateRequest();
        request.setToken("tok");
        request.setClientSecret("not-uuid");

        ResponseEntity<ValidateResponse> response = controller.validate(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isValid()).isFalse();
        verifyNoInteractions(useCase);
    }

    @Test
    void validateReturnsUnauthorizedWhenTokenInvalid() {
        ValidateRequest request = new ValidateRequest();
        request.setToken("tok");
        request.setClientSecret("123e4567-e89b-12d3-a456-426614174000");
        Credentials credentials = Credentials.builder()
                .clientSecret("123e4567-e89b-12d3-a456-426614174000")
                .build();
        TokenData tokenData = TokenData.builder().token("tok").build();
        when(useCase.validateToken(tokenData, credentials)).thenReturn(false);

        ResponseEntity<ValidateResponse> response = controller.validate(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isValid()).isFalse();
        verify(useCase).validateToken(tokenData, credentials);
    }
}
