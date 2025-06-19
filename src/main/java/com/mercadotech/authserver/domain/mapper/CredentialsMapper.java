package com.mercadotech.authserver.domain.mapper;

import com.mercadotech.authserver.domain.model.Credentials;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CredentialsMapper {
    public Credentials from(String clientId, String clientSecret) {
        return Credentials.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
