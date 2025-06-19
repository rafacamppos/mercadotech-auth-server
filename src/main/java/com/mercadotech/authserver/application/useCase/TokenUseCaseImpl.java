package com.mercadotech.authserver.application.useCase;

import com.mercadotech.authserver.application.service.TokenService;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUseCaseImpl implements TokenUseCase {

    private final TokenService tokenService;

    @Override
    public TokenData generateToken(Credentials credentials) {
        return tokenService.generateToken(credentials);
    }

    @Override
    public boolean validateToken(TokenData tokenData, Credentials credentials) {
        return tokenService.validateToken(tokenData, credentials);
    }
}
