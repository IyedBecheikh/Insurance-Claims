package com.iyed.insuranceclaims.client.controller;

import com.iyed.insuranceclaims.client.dto.ClientResponseDto;
import com.iyed.insuranceclaims.client.dto.CreateClientRequestDto;
import com.iyed.insuranceclaims.client.dto.UpdateClientRequestDto;
import com.iyed.insuranceclaims.client.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a client profile")
    public ClientResponseDto create(@Valid @RequestBody CreateClientRequestDto request) {
        return clientService.create(request);
    }

    @GetMapping
    @Operation(summary = "List clients")
    public List<ClientResponseDto> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by id")
    public ClientResponseDto findById(@PathVariable UUID id) {
        return clientService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client profile")
    public ClientResponseDto update(@PathVariable UUID id, @Valid @RequestBody UpdateClientRequestDto request) {
        return clientService.update(id, request);
    }
}
