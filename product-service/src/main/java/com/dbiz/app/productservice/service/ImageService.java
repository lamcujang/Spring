package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.ImageDto;

import java.util.Optional;

public interface ImageService {
    ImageDto save(final ImageDto image);

    ImageDto findById(final Integer imageId);

    void deleteById(final Integer imageId);
}
