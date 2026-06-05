package com.iyed.insuranceclaims.client.service;

import com.iyed.insuranceclaims.client.dto.ClientResponseDto;
import com.iyed.insuranceclaims.client.dto.CreateClientRequestDto;
import com.iyed.insuranceclaims.client.dto.UpdateClientRequestDto;
import com.iyed.insuranceclaims.client.entity.Client;
import com.iyed.insuranceclaims.client.mapper.ClientMapper;
import com.iyed.insuranceclaims.client.repository.ClientRepository;
import com.iyed.insuranceclaims.common.exception.ResourceNotFoundException;
import com.iyed.insuranceclaims.user.entity.Role;
import com.iyed.insuranceclaims.user.entity.User;
import com.iyed.insuranceclaims.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserService userService;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, UserService userService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.userService = userService;
    }

    public ClientResponseDto create(CreateClientRequestDto request) {
        User user = userService.getUser(request.userId());
        if (user.getRole() != Role.CLIENT) {
            throw new IllegalArgumentException("Client profile can only be linked to a CLIENT user");
        }
        clientRepository.findByUserId(request.userId()).ifPresent(existing -> {
            throw new IllegalStateException("Client profile already exists for this user");
        });

        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setUser(user);
        client.setFirstName(request.firstName());
        client.setLastName(request.lastName());
        client.setPhone(request.phone());
        client.setAddress(request.address());
        client.setNationalId(request.nationalId());
        client.setDateOfBirth(request.dateOfBirth());
        client.setRegistrationDate(LocalDateTime.now());
        return clientMapper.toResponseDto(clientRepository.save(client));
    }

    public List<ClientResponseDto> findAll() {
        return clientRepository.findAll().stream().map(clientMapper::toResponseDto).toList();
    }

    public ClientResponseDto findById(UUID id) {
        return clientMapper.toResponseDto(getClient(id));
    }

    public ClientResponseDto update(UUID id, UpdateClientRequestDto request) {
        Client client = getClient(id);
        client.setFirstName(request.firstName());
        client.setLastName(request.lastName());
        client.setPhone(request.phone());
        client.setAddress(request.address());
        client.setNationalId(request.nationalId());
        client.setDateOfBirth(request.dateOfBirth());
        return clientMapper.toResponseDto(clientRepository.save(client));
    }

    public Client getClient(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }
}
