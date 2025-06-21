package com.mercadotech.authserver.adapter.mapper;

import com.mercadotech.authserver.adapter.server.dto.LoginRequest;
import com.mercadotech.authserver.adapter.server.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.server.mapper.CredentialsMapper;
import com.mercadotech.authserver.domain.model.Credentials;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CredentialsMapperTest {

    @Test
    void fromLoginRequestMapsAllFields() {
        LoginRequest request = LoginRequest.builder()
                .clientId("id")
                .clientSecret("sec")
                .build();

        Credentials result = CredentialsMapper.from(request);

        assertThat(result.getClientId()).isEqualTo("id");
        assertThat(result.getClientSecret()).isEqualTo("sec");
    }

    @Test
    void fromValidateRequestMapsSecret() {
        ValidateRequest request = ValidateRequest.builder()
                .token("tok")
                .clientId("sec")
                .build();

        Credentials result = CredentialsMapper.from(request);

        assertThat(result.getClientId()).isNull();
        assertThat(result.getClientSecret()).isEqualTo("sec");
    }
}
