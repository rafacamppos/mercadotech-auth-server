package com.mercadotech.authserver.application.service;

import com.mercadotech.authserver.adapter.database.entity.CredentialsEntity;
import com.mercadotech.authserver.adapter.database.repository.CredentialsRepository;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.exception.DataBaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialsServiceImpl implements CredentialsService {

    private final CredentialsRepository repository;

    @Override
    public void save(Credentials credentials) {
        CredentialsEntity entity = CredentialsEntity.builder()
                .clientId(credentials.getClientId())
                .clientSecret(credentials.getClientSecret())
                .build();
        try {
            repository.save(entity);
        } catch (Exception e) {
            throw new DataBaseException("Failed to save credentials", e);
        }
    }
}
