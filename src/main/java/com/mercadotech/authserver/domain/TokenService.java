package com.mercadotech.authserver.domain;

import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;

public interface TokenService {
    TokenData generateToken(Credentials credentials);
    boolean validateToken(TokenData tokenData, Credentials credentials);
}
