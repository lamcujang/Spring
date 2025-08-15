package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.BusinessSector;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.BusinessSectorDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class BusinessSectorMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<BusinessSectorDto, BusinessSector>() {
            @Override
            protected void configure() {
                map(source.getBusinessSectorGroupId(), destination.getBusinessSectorGroupId());
            }
        });

        modelMapper.addMappings(new PropertyMap<BusinessSector, BusinessSectorDto>() {
            @Override
            protected void configure() {
                map(source.getBusinessSectorGroupId(), destination.getBusinessSectorGroupId());
            }
        });
    }

    public BusinessSector toBusinessSector(final BusinessSectorDto dto)
    {
        return modelMapper.map(dto, BusinessSector.class);
    }


    public BusinessSector updateEntity(BusinessSectorDto dto, BusinessSector entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public BusinessSectorDto toBusinessSectorDto(final BusinessSector entity)
    {
        return modelMapper.map(entity, BusinessSectorDto.class);
    }

}
