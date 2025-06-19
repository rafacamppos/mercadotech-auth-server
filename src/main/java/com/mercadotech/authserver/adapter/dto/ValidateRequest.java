package com.mercadotech.authserver.adapter.dto;

import lombok.Data;

@Data
public class ValidateRequest {
    private String token;
    private String clientSecret;
}
