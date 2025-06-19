package com.mercadotech.authserver.domain.mapper;

import com.mercadotech.authserver.domain.model.TokenData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenDataMapperTest {

    @Test
    void fromMapsToken() {
        TokenData data = TokenDataMapper.from("tok");

        assertThat(data.getToken()).isEqualTo("tok");
    }
}
