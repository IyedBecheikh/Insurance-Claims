package com.iyed.insuranceclaims.client.repository;

import com.iyed.insuranceclaims.client.entity.Client;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByUserId(UUID userId);
}
