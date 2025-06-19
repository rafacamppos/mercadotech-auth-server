package com.mercadotech.authserver.adapter.database.repository;

import com.mercadotech.authserver.adapter.database.entity.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, String> {
}
