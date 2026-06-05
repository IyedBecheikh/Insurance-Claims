package com.iyed.insuranceclaims.contract.mapper;

import com.iyed.insuranceclaims.client.entity.Client;
import com.iyed.insuranceclaims.common.mapper.CentralMapperConfig;
import com.iyed.insuranceclaims.contract.dto.ContractResponseDto;
import com.iyed.insuranceclaims.contract.entity.Contract;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface ContractMapper {

    @Mapping(target = "clientId", source = "client")
    ContractResponseDto toResponseDto(Contract contract);

    default UUID map(Client client) {
        return client == null ? null : client.getId();
    }
}
