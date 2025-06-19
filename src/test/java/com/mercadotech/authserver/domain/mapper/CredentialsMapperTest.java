package com.mercadotech.authserver.domain.mapper;

import com.mercadotech.authserver.domain.model.Credentials;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CredentialsMapperTest {

    @Test
    void fromMapsValues() {
        Credentials credentials = CredentialsMapper.from("id", "secret");

        assertThat(credentials.getClientId()).isEqualTo("id");
        assertThat(credentials.getClientSecret()).isEqualTo("secret");
    }
}
