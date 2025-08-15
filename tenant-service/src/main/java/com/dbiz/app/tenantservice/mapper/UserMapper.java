package com.dbiz.app.tenantservice.mapper;

import com.dbiz.app.tenantservice.domain.Tenant;
import com.dbiz.app.tenantservice.domain.db.User1;
import com.dbiz.app.tenantservice.dto.reponse.CreateUserResponseDto;
import com.dbiz.app.tenantservice.dto.request.CreateUserRequestDto;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User1 fromRequestDto(CreateUserRequestDto requestDto);

    CreateUserResponseDto toResponseDto(User1 user);

    TenantDto toDto(Tenant tenant);
}
