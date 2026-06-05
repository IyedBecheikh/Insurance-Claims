package com.iyed.insuranceclaims.document.mapper;

import com.iyed.insuranceclaims.claim.entity.Claim;
import com.iyed.insuranceclaims.common.mapper.CentralMapperConfig;
import com.iyed.insuranceclaims.document.dto.ClaimDocumentResponseDto;
import com.iyed.insuranceclaims.document.entity.ClaimDocument;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface ClaimDocumentMapper {

    @Mapping(target = "claimId", source = "claim")
    ClaimDocumentResponseDto toResponseDto(ClaimDocument document);

    default UUID map(Claim claim) {
        return claim == null ? null : claim.getId();
    }
}
