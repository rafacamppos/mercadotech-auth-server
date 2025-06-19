package com.mercadotech.authserver.application.useCase;

import com.mercadotech.authserver.domain.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUseCaseImpl implements TokenUseCase {

    private final TokenService tokenService;

    @Override
    public String generateToken(String clientId, String clientSecret) {
        return tokenService.generateToken(clientId, clientSecret);
    }

    @Override
    public boolean validateToken(String token, String clientSecret) {
        return tokenService.validateToken(token, clientSecret);
    }
}
