package com.mercadotech.authserver.adapter.server.controller;

import com.mercadotech.authserver.adapter.server.dto.LoginRequest;
import com.mercadotech.authserver.adapter.server.dto.TokenResponse;
import com.mercadotech.authserver.adapter.server.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.server.dto.ValidateResponse;
import com.mercadotech.authserver.adapter.server.mapper.CredentialsMapper;
import com.mercadotech.authserver.adapter.server.mapper.TokenMapper;
import com.mercadotech.authserver.adapter.server.mapper.TokenResponseMapper;
import com.mercadotech.authserver.application.useCase.TokenUseCase;
import com.mercadotech.authserver.application.service.CredentialsService;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import java.util.UUID;

import com.mercadotech.authserver.logging.DefaultStructuredLogger;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import com.mercadotech.authserver.logging.StructuredLogger;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {

    private final TokenUseCase tokenUseCase;
    private final Counter tokensIssuedCounter;
    private final Timer loginTimer;
    private final Timer validateTimer;
    private final CredentialsService credentialsService;
    private final StructuredLogger logger = new DefaultStructuredLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        try {
            validateUuid(request.getClientId());
            validateUuid(request.getClientSecret());
            return loginTimer.record(() -> {
                Credentials credentials = CredentialsMapper.from(request);
                credentialsService.save(credentials);
                TokenData tokenData = tokenUseCase.generateToken(credentials);
                tokensIssuedCounter.increment();
                logger.info(String.format("Login success for client %s", credentials.getClientId()), null);
                TokenResponse response = TokenResponseMapper.from(tokenData);
                return ResponseEntity.ok(response);
            });
        } catch (Exception e) {
            logger.error(String.format("Login failed for client %s", request.getClientId()), null, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/token/validate")
    public ResponseEntity<ValidateResponse> validate(@RequestBody ValidateRequest request) {
        return validateTimer.record(() -> {
            try {
                validateUuid(request.getClientSecret());
            } catch (Exception e) {
                logger.error("Invalid clientSecret format", null, e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ValidateResponse.builder().valid(false).build());
            }
            Credentials credentials = CredentialsMapper.from(request);
            TokenData tokenData = TokenMapper.from(request);
            boolean valid = tokenUseCase.validateToken(tokenData, credentials);
            if (valid) {
                logger.info("Token validation success", null);
                return ResponseEntity.ok(ValidateResponse.builder().valid(true).build());
            } else {
                logger.warn("Token validation failed", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ValidateResponse.builder().valid(false).build());
            }
        });
    }

    private void validateUuid(String value) {
        UUID.fromString(value);
    }
}
