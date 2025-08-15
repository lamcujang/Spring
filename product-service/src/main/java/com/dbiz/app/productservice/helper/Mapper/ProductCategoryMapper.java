package com.dbiz.app.productservice.helper.Mapper;


import com.dbiz.app.productservice.domain.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ProductCategoryMapper {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<ProductCategoryDto, ProductCategory>() {
            @Override
            protected void configure() {
                map(source.getProductCategoryParentId(), destination.getProductCategoryParentId());

            }
        });

        modelMapper.addMappings(new PropertyMap<ProductCategory, ProductCategoryDto>() {
            @Override
            protected void configure() {
                map(source.getProductCategoryParentId(), destination.getProductCategoryParentId());
            }
        });
    }
    public ProductCategory toProductCategory(final ProductCategoryDto productCategoryDto)
    {
        return modelMapper.map(productCategoryDto, ProductCategory.class);
    }

    public ProductCategory updateEntity(ProductCategoryDto dto, ProductCategory entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public ProductCategoryDto toProductCategoryDto(final ProductCategory productCategory)
    {
        return modelMapper.map(productCategory, ProductCategoryDto.class);
    }
}
