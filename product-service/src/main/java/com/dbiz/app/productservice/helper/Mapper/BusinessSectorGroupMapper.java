package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.BusinessSectorGroup;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class BusinessSectorGroupMapper {
    private final ModelMapper modelMapper;

    public BusinessSectorGroup toBusinessSectorGroup(final BusinessSectorGroupDto dto)
    {
        return modelMapper.map(dto, BusinessSectorGroup.class);
    }

    public BusinessSectorGroup updateEntity(BusinessSectorGroupDto dto, BusinessSectorGroup entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public BusinessSectorGroupDto toBusinessSectorGroupDto(final BusinessSectorGroup entity)
    {
        return modelMapper.map(entity, BusinessSectorGroupDto.class);
    }

}
