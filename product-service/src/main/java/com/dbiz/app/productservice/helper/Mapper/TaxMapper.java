package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.Tax;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.TaxDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TaxMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<TaxDto, Tax>() {
            @Override
            protected void configure() {
                map(source.getTaxCategoryId(), destination.getTaxCategoryId());
            }
        });

        modelMapper.addMappings(new PropertyMap<Tax, TaxDto>() {
            @Override
            protected void configure() {
                map(source.getTaxCategoryId(), destination.getTaxCategoryId());
            }
        });
    }

    public Tax toTax(final TaxDto dto)
    {
        return modelMapper.map(dto, Tax.class);
    }


    public Tax updateEntity(TaxDto dto, Tax entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public TaxDto toTaxDto(final Tax entity)
    {
        return modelMapper.map(entity, TaxDto.class);
    }

}
