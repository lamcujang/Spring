package com.dbiz.app.tenantservice.helper;

import com.dbiz.app.tenantservice.domain.Tenant;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantMapper {
    private final ModelMapper modelMapper;
    public Tenant toTenant(final TenantDto tenantDto)
    {
        return modelMapper.map(tenantDto, Tenant.class);
    }


    public Tenant updateEntity(TenantDto dto, Tenant entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public TenantDto toProductCategoryDto(final Tenant uom)
    {
        return modelMapper.map(uom, TenantDto.class);
    }
}
