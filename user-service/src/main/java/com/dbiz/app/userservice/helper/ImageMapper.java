package com.dbiz.app.userservice.helper;

import com.dbiz.app.userservice.domain.Image;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageMapper {
    private final ModelMapper modelMapper;

    public Image toImg(ImageDto imageDto){
        return modelMapper.map(imageDto, Image.class);
    }

    public  ImageDto toImageDto(Image entity){
        return modelMapper.map(entity, ImageDto.class);
    }

    public Image updateEntity(ImageDto dto, Image entity){
        modelMapper.map(dto, entity);
        return entity;
    }
}
