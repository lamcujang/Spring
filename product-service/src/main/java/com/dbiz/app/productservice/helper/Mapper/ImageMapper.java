package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.Image;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageMapper {
    private final ModelMapper modelMapper;

    public Image toImage(final ImageDto imageDto)
    {
        return modelMapper.map(imageDto, Image.class);
    }
    public Image updateEntity(ImageDto dto, Image entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
}
