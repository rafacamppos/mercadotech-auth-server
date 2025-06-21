package com.mercadotech.authserver.adapter.mapper;

import com.mercadotech.authserver.adapter.server.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.server.mapper.TokenMapper;
import com.mercadotech.authserver.domain.model.TokenData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenMapperTest {

    @Test
    void fromValidateRequestMapsToken() {
        ValidateRequest request = ValidateRequest.builder()
                .token("tok")
                .clientId("secret")
                .build();

        TokenData result = TokenMapper.from(request);

        assertThat(result.getToken()).isEqualTo("tok");
    }
}
