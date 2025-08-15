package com.dbiz.app.productservice.helper;

import com.dbiz.app.productservice.domain.Image;
import org.common.dbiz.dto.productDto.ImageDto;

public interface ImageMappingHelper {
    public static ImageDto map(Image dimage)
    {
        return ImageDto.builder()
                .id(dimage.getId() != null ? dimage.getId() : null)
                .imageUrl(dimage.getImageUrl())
                .build();
    }

    public static Image map(ImageDto dimage)
    {
        return Image.builder()
                .id(dimage.getId() != null ? dimage.getId() : null)
                .imageUrl(dimage.getImageUrl())
                .build();
    }
}
