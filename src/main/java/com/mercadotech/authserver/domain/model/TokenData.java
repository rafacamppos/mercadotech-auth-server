package com.mercadotech.authserver.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenData {
    String token;
}
