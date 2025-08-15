package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.PriceListProduct;
import com.dbiz.app.productservice.domain.Product;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.PriceListProductDto;
import org.common.dbiz.dto.productDto.ProductChildDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class PriceListProductMapper{
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<PriceListProductDto, PriceListProduct>() {
            @Override
            protected void configure() {
                map(source.getProductId(), destination.getProductId());
                map(source.getPriceListId(), destination.getPriceListId());
            }
        });




        modelMapper.addMappings(new PropertyMap<PriceListProduct, PriceListProductDto>() {
            @Override
            protected void configure() {
                map(source.getProductId(), destination.getProductId());
                map(source.getPriceListId(), destination.getPriceListId());
            }
        });
    }
    public PriceListProduct toEntity(final PriceListProductDto dto)
    {

        return modelMapper.map(dto, PriceListProduct.class);
    }
    public PriceListProduct updateEntity(PriceListProductDto dto, PriceListProduct entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public PriceListProductDto toDto(final PriceListProduct product)
    {
        return modelMapper.map(product, PriceListProductDto.class);
    }
}
