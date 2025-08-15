package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.Product;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ProductChildDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<ProductDto , Product>() {
            @Override
            protected void configure() {
                map(source.getTaxId(), destination.getTaxId());
//                map(source.getProductCategory().getId(), destination.getProductCategory().getId());
//                map(source.getProductCategory().getProductCategoryParentId(), destination.getProductCategory().getProductCategoryParentId());
                
            }
        });




        modelMapper.addMappings(new PropertyMap<Product, ProductDto>() {
            @Override
            protected void configure() {
                map(source.getTaxId(), destination.getTaxId());
//                map(source.getProductCategory().getId(), destination.getProductCategory().getId());
//                map(source.getProductCategory().getProductCategoryParentId(), destination.getProductCategory().getProductCategoryParentId());
            }
        });
    }
    public Product toProduct(final ProductDto productDto)
    {

        return modelMapper.map(productDto, Product.class);
    }
    public Product updateEntity(ProductDto dto, Product entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public ProductDto toDProductDto(final Product product)
    {
        return modelMapper.map(product, ProductDto.class);
    }
}
