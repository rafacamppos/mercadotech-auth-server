package com.mercadotech.authserver.adapter;

import com.mercadotech.authserver.adapter.dto.LoginRequest;
import com.mercadotech.authserver.adapter.dto.TokenResponse;
import com.mercadotech.authserver.adapter.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.dto.ValidateResponse;
import com.mercadotech.authserver.adapter.mapper.CredentialsMapper;
import com.mercadotech.authserver.adapter.mapper.TokenMapper;
import com.mercadotech.authserver.adapter.mapper.TokenResponseMapper;
import com.mercadotech.authserver.application.useCase.TokenUseCase;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenUseCase tokenUseCase;
    private final Counter tokensIssuedCounter;
    private final Timer loginTimer;

    public AuthController(TokenUseCase tokenUseCase, MeterRegistry registry) {
        this.tokenUseCase = tokenUseCase;
        this.tokensIssuedCounter = Counter.builder("auth_tokens_issued")
                .description("Number of tokens issued")
                .register(registry);
        this.loginTimer = Timer.builder("auth_login_latency")
                .description("Latency of login endpoint")
                .register(registry);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        try {
            return loginTimer.record(() -> {
                Credentials credentials = CredentialsMapper.from(request);
                TokenData tokenData = tokenUseCase.generateToken(credentials);
                tokensIssuedCounter.increment();
                logger.info("Login success for client {}", credentials.getClientId());
                TokenResponse response = TokenResponseMapper.from(tokenData);
                return ResponseEntity.ok(response);
            });
        } catch (Exception e) {
            logger.error("Login failed for client {}", request.getClientId(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/token/validate")
    public ResponseEntity<ValidateResponse> validate(@RequestBody ValidateRequest request) {
        Credentials credentials = CredentialsMapper.from(request);
        TokenData tokenData = TokenMapper.from(request);
        boolean valid = tokenUseCase.validateToken(tokenData, credentials);
        if (valid) {
            logger.info("Token validation success");
        } else {
            logger.warn("Token validation failed");
        }
        return ResponseEntity.ok(ValidateResponse.builder().valid(valid).build());
    }
}
