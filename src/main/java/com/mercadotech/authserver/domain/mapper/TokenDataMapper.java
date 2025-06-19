package com.mercadotech.authserver.domain.mapper;

import com.mercadotech.authserver.domain.model.TokenData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenDataMapper {
    public TokenData from(String token) {
        return TokenData.builder()
                .token(token)
                .build();
    }
}
