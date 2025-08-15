package com.dbiz.app.userservice.service;



import org.common.dbiz.dto.productDto.ImageDto;



public interface ImageService {

    ImageDto save(final ImageDto image);

    void deleteById(final Integer imageId);

    ImageDto findById(final Integer imageId);

}
