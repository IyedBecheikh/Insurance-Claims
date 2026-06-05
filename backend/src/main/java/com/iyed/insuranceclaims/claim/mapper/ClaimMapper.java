package com.iyed.insuranceclaims.claim.mapper;

import com.iyed.insuranceclaims.claim.dto.ClaimResponseDto;
import com.iyed.insuranceclaims.claim.entity.Claim;
import com.iyed.insuranceclaims.client.entity.Client;
import com.iyed.insuranceclaims.common.mapper.CentralMapperConfig;
import com.iyed.insuranceclaims.contract.entity.Contract;
import com.iyed.insuranceclaims.document.mapper.ClaimDocumentMapper;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class, uses = {ClaimDocumentMapper.class, ClaimCommentMapper.class})
public interface ClaimMapper {

    @Mapping(target = "clientId", source = "client")
    @Mapping(target = "contractId", source = "contract")
    @Mapping(
            target = "reviewedBy",
            expression = "java(claim.getReviewedBy() != null ? claim.getReviewedBy().getId() : null)")
    ClaimResponseDto toResponseDto(Claim claim);

    default UUID map(Client client) {
        return client == null ? null : client.getId();
    }

    default UUID map(Contract contract) {
        return contract == null ? null : contract.getId();
    }
}
