package com.iyed.insuranceclaims.contract.repository;

import com.iyed.insuranceclaims.contract.entity.Contract;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    Optional<Contract> findByContractNumber(String contractNumber);

    List<Contract> findByClientId(UUID clientId);
}
