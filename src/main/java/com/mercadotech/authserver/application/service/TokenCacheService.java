package com.mercadotech.authserver.application.service;

public interface TokenCacheService {
    void save(String clientId, String token);

    String get(String clientId);
}
