package com.mercadotech.authserver.application.useCase;

public interface TokenUseCase {
    String generateToken(String clientId, String clientSecret);
    boolean validateToken(String token, String clientSecret);
}
