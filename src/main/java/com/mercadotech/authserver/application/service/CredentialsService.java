package com.mercadotech.authserver.application.service;

import com.mercadotech.authserver.domain.model.Credentials;

public interface CredentialsService {
    void save(Credentials credentials);
}
