package com.dbiz.app.integrationservice.helper.mapper;

import com.dbiz.app.integrationservice.domain.ErpIntegration;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ErpIntegrationMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {

        modelMapper.addMappings(new PropertyMap<ErpIntegrationDto, ErpIntegration>() {
            @Override
            protected void configure() {
                map(source.getAdOrgId(), destination.getAdOrgId());
                map(source.getOrgId(),destination.getOrgId());
            }
        });

        modelMapper.addMappings(new PropertyMap<ErpIntegration,ErpIntegrationDto>() {
            @Override
            protected void configure() {
                map(source.getAdOrgId(), destination.getAdOrgId());
                map(source.getOrgId(),destination.getOrgId());
            }
        });
    }

    public ErpIntegration toEntity(ErpIntegrationDto dto) {
        return modelMapper.map(dto, ErpIntegration.class);
    }

    public ErpIntegrationDto toDto(ErpIntegration entity) {
        return modelMapper.map(entity, ErpIntegrationDto.class);
    }

    public ErpIntegration updateEntity(ErpIntegrationDto dto, ErpIntegration entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

}
