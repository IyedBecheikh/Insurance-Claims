package com.iyed.insuranceclaims.user.mapper;

import com.iyed.insuranceclaims.common.mapper.CentralMapperConfig;
import com.iyed.insuranceclaims.user.dto.UserResponseDto;
import com.iyed.insuranceclaims.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface UserMapper {

    UserResponseDto toResponseDto(User user);
}
