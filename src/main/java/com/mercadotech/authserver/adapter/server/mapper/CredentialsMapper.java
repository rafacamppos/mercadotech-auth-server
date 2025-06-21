package com.mercadotech.authserver.adapter.server.mapper;

import com.mercadotech.authserver.adapter.server.dto.LoginRequest;
import com.mercadotech.authserver.adapter.server.dto.ValidateRequest;
import com.mercadotech.authserver.domain.model.Credentials;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CredentialsMapper {
    public Credentials from(LoginRequest request) {
        return Credentials.builder()
                .clientId(request.getClientId())
                .clientSecret(request.getClientSecret())
                .build();
    }

    public Credentials from(ValidateRequest request) {
        return Credentials.builder()
                .clientSecret(request.getClientId())
                .build();
    }
}
