package com.iyed.insuranceclaims.contract.service;

import com.iyed.insuranceclaims.client.entity.Client;
import com.iyed.insuranceclaims.client.repository.ClientRepository;
import com.iyed.insuranceclaims.client.service.ClientService;
import com.iyed.insuranceclaims.common.exception.ResourceNotFoundException;
import com.iyed.insuranceclaims.contract.dto.ContractResponseDto;
import com.iyed.insuranceclaims.contract.dto.CreateContractRequestDto;
import com.iyed.insuranceclaims.contract.dto.UpdateContractRequestDto;
import com.iyed.insuranceclaims.contract.entity.Contract;
import com.iyed.insuranceclaims.contract.entity.ContractStatus;
import com.iyed.insuranceclaims.contract.mapper.ContractMapper;
import com.iyed.insuranceclaims.contract.repository.ContractRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public ContractService(
            ContractRepository contractRepository,
            ContractMapper contractMapper,
            ClientService clientService,
            ClientRepository clientRepository) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    public ContractResponseDto create(CreateContractRequestDto request) {
        validateDates(request.startDate(), request.endDate());
        contractRepository.findByContractNumber(request.contractNumber()).ifPresent(existing -> {
            throw new IllegalStateException("Contract number already exists");
        });

        Client client = clientService.getClient(request.clientId());

        Contract contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setClient(client);
        contract.setContractNumber(request.contractNumber());
        contract.setType(request.type());
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setCoverageLimit(request.coverageLimit());
        contract.setReimbursementRate(request.reimbursementRate());
        contract.setStatus(request.status());
        return contractMapper.toResponseDto(contractRepository.save(contract));
    }

    public List<ContractResponseDto> findAll() {
        return contractRepository.findAll().stream().map(contractMapper::toResponseDto).toList();
    }

    public List<ContractResponseDto> findOwnContracts(UUID userId) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client profile not found"));
        return contractRepository.findByClientId(client.getId()).stream()
                .map(contractMapper::toResponseDto)
                .toList();
    }

    public ContractResponseDto findById(UUID id) {
        return contractMapper.toResponseDto(getContract(id));
    }

    public ContractResponseDto update(UUID id, UpdateContractRequestDto request) {
        validateDates(request.startDate(), request.endDate());
        Contract contract = getContract(id);
        Client client = clientService.getClient(request.clientId());

        contractRepository.findByContractNumber(request.contractNumber())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("Contract number already exists");
                });

        contract.setClient(client);
        contract.setContractNumber(request.contractNumber());
        contract.setType(request.type());
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setCoverageLimit(request.coverageLimit());
        contract.setReimbursementRate(request.reimbursementRate());
        return contractMapper.toResponseDto(contractRepository.save(contract));
    }

    public ContractResponseDto activate(UUID id) {
        Contract contract = getContract(id);
        if (contract.getStatus() == ContractStatus.EXPIRED) {
            throw new IllegalStateException("Expired contracts cannot be activated");
        }
        contract.setStatus(ContractStatus.ACTIVE);
        return contractMapper.toResponseDto(contractRepository.save(contract));
    }

    public ContractResponseDto suspend(UUID id) {
        Contract contract = getContract(id);
        if (contract.getStatus() == ContractStatus.EXPIRED) {
            throw new IllegalStateException("Expired contracts cannot be suspended");
        }
        contract.setStatus(ContractStatus.SUSPENDED);
        return contractMapper.toResponseDto(contractRepository.save(contract));
    }

    private Contract getContract(UUID id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Contract end date must be on or after the start date");
        }
    }
}
