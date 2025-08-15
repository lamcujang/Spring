package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.Uom;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.UomDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UomMapper {
    private final ModelMapper modelMapper;
    public Uom toUom(final UomDto  uomDto)
    {
        return modelMapper.map(uomDto, Uom.class);
    }


    public Uom updateEntity(UomDto dto, Uom entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public UomDto toProductCategoryDto(final Uom uom)
    {
        return modelMapper.map(uom, UomDto.class);
    }
}
