package com.mercadotech.authserver.application.useCase;

import com.mercadotech.authserver.application.service.TokenService;
import com.mercadotech.authserver.application.service.TokenCacheService;
import com.mercadotech.authserver.logging.DefaultStructuredLogger;
import com.mercadotech.authserver.logging.StructuredLogger;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUseCaseImpl implements TokenUseCase {

    private final TokenService tokenService;
    private final TokenCacheService tokenCacheService;
    private final StructuredLogger logger = new DefaultStructuredLogger(TokenUseCaseImpl.class);

    @Override
    public TokenData generateToken(Credentials credentials) {
        TokenData tokenData = tokenService.generateToken(credentials);
        tokenCacheService.save(credentials.getClientId(), tokenData.getToken());
        logger.info(String.format("Token generated for client %s", credentials.getClientId()), null);
        return tokenData;
    }

    @Override
    public boolean validateToken(TokenData tokenData, Credentials credentials) {
        return tokenService.validateToken(tokenData, credentials);
    }
}
