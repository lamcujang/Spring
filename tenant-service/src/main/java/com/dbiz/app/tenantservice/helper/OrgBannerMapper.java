package com.dbiz.app.tenantservice.helper;


import com.dbiz.app.tenantservice.domain.OrgBanner;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.tenantDto.OrgBannerDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class OrgBannerMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<OrgBannerDto, OrgBanner>() {
            @Override
            protected void configure() {
                map(source.getImageId(), destination.getImageId());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrgBanner,OrgBannerDto>() {
            @Override
            protected void configure() {
                map(source.getImageId(), destination.getImageId());
            }
        });
    }

    public OrgBanner toEntity(OrgBannerDto dto) {
        return modelMapper.map(dto, OrgBanner.class);
    }

    public OrgBannerDto toDto(OrgBanner entity) {
        return modelMapper.map(entity, OrgBannerDto.class);
    }

    public OrgBanner updateEntity(OrgBannerDto dto, OrgBanner entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

}
