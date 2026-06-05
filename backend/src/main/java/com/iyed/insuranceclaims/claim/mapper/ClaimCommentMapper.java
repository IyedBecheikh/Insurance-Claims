package com.iyed.insuranceclaims.claim.mapper;

import com.iyed.insuranceclaims.claim.dto.ClaimCommentResponseDto;
import com.iyed.insuranceclaims.claim.entity.Claim;
import com.iyed.insuranceclaims.claim.entity.ClaimComment;
import com.iyed.insuranceclaims.common.mapper.CentralMapperConfig;
import com.iyed.insuranceclaims.user.entity.User;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface ClaimCommentMapper {

    @Mapping(target = "claimId", source = "claim")
    @Mapping(target = "authorId", source = "author")
    ClaimCommentResponseDto toResponseDto(ClaimComment comment);

    default UUID map(Claim claim) {
        return claim == null ? null : claim.getId();
    }

    default UUID map(User user) {
        return user == null ? null : user.getId();
    }
}
