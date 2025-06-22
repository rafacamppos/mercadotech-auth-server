package com.mercadotech.authserver.client;

import com.mercadotech.authserver.adapter.server.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.server.dto.ValidateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuthClient(RestTemplate restTemplate,
                      @Value("${auth.server.url:http://localhost:8080}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public boolean validateToken(String token, String clientId) {
        ValidateRequest request = ValidateRequest.builder()
                .token(token)
                .clientId(clientId)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ValidateRequest> entity = new HttpEntity<>(request, headers);
        ValidateResponse response = restTemplate.postForObject(
                baseUrl + "/token/validate", entity, ValidateResponse.class);
        return response != null && response.isValid();
    }
}
