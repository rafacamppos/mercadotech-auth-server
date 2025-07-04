package com.mercadotech.authserver.adapter.server.mapper;


import com.mercadotech.authserver.adapter.server.dto.ValidateRequest;
import com.mercadotech.authserver.domain.model.TokenData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenMapper {
    public TokenData from(ValidateRequest request) {
        return TokenData.builder()
                .token(request.getToken())
                .build();
    }
}
