package com.dbiz.app.tenantservice.mapper;


import com.dbiz.app.tenantservice.domain.db.Tenant1;

import com.dbiz.app.tenantservice.dto.request.CreateTenantRequestDto;
import org.common.dbiz.dto.tenantDto.reponse.TenantResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper
public interface TenantMapper {

    TenantMapper INSTANCE = Mappers.getMapper(TenantMapper.class);

    Tenant1 fromRequestDto(CreateTenantRequestDto requestDto);

    @Mapping(target = "userId", ignore = true)
    TenantResponseDto toResponseDto(Tenant1 tenant);

    List<TenantResponseDto> toResponseDtoList(List<Tenant1> tenants);
}
