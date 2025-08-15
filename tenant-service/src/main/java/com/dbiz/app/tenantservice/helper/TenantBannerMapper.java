package com.dbiz.app.tenantservice.helper;


import com.dbiz.app.tenantservice.domain.OrgBanner;
import com.dbiz.app.tenantservice.domain.TenantBanner;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.tenantDto.OrgBannerDto;
import org.common.dbiz.dto.tenantDto.TenantBannerDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TenantBannerMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<TenantBannerDto, TenantBanner>() {
            @Override
            protected void configure() {
                map(source.getImageId(), destination.getImageId());
            }
        });

        modelMapper.addMappings(new PropertyMap<TenantBanner,TenantBannerDto>() {
            @Override
            protected void configure() {
                map(source.getImageId(), destination.getImageId());
            }
        });
    }

    public TenantBanner toEntity(TenantBannerDto dto) {
        return modelMapper.map(dto, TenantBanner.class);
    }

    public TenantBannerDto toDto(TenantBanner entity) {
        return modelMapper.map(entity, TenantBannerDto.class);
    }

    public TenantBanner updateEntity(TenantBannerDto dto, TenantBanner entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

}
