package com.mercadotech.authserver.adapter.mapper;

import com.mercadotech.authserver.adapter.dto.TokenResponse;
import com.mercadotech.authserver.domain.model.TokenData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenResponseMapperTest {

    @Test
    void fromTokenDataMapsToken() {
        TokenData data = TokenData.builder().token("tok").build();

        TokenResponse response = TokenResponseMapper.from(data);

        assertThat(response.getToken()).isEqualTo("tok");
    }
}
