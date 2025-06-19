package com.mercadotech.authserver.application.useCase;

import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;

public interface TokenUseCase {
    TokenData generateToken(Credentials credentials);
    boolean validateToken(TokenData tokenData, Credentials credentials);
}
