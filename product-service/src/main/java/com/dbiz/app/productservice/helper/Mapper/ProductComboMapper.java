package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.ProductCombo;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ProductComboDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ProductComboMapper {

    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<ProductComboDto , ProductCombo>() {
            @Override
            protected void configure() {
                map(source.getProductId(), destination.getProductId());
            }
        });

        modelMapper.addMappings(new PropertyMap<ProductCombo, ProductComboDto>() {
            @Override
            protected void configure() {
                map(source.getProductId(), destination.getProductId());
            }
        });
    }

    public ProductCombo toProductCombo(final ProductComboDto productComboDto)
    {
        return modelMapper.map(productComboDto, ProductCombo.class);
    }

    public ProductCombo updateEntity(ProductComboDto dto, ProductCombo entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public ProductComboDto toProductComboDto(final ProductCombo productCombo)
    {
        return modelMapper.map(productCombo, ProductComboDto.class);
    }

}
