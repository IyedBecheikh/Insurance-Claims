package com.iyed.insuranceclaims.client.mapper;

import com.iyed.insuranceclaims.client.dto.ClientResponseDto;
import com.iyed.insuranceclaims.client.entity.Client;
import com.iyed.insuranceclaims.common.mapper.CentralMapperConfig;
import com.iyed.insuranceclaims.user.entity.User;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface ClientMapper {

    @Mapping(target = "userId", source = "user")
    ClientResponseDto toResponseDto(Client client);

    default UUID map(User user) {
        return user == null ? null : user.getId();
    }
}
