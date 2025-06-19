package com.mercadotech.authserver.domain;

public interface TokenService {
    String generateToken(String clientId, String clientSecret);
    boolean validateToken(String token, String clientSecret);
}
