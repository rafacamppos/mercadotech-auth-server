package com.mercadotech.authserver.adapter.mapper;

import com.mercadotech.authserver.adapter.dto.TokenResponse;
import com.mercadotech.authserver.domain.model.TokenData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenResponseMapper {
    public TokenResponse from(TokenData tokenData) {
        return TokenResponse.builder()
                .token(tokenData.getToken())
                .build();
    }
}
