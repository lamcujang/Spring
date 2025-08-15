package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.userservice.domain.Image;
import com.dbiz.app.userservice.helper.ImageHelper;
import com.dbiz.app.userservice.repository.ImageRepository;
import com.dbiz.app.userservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageHelper imageHelper;

    private final ModelMapper modelMapper;

    private final ImageRepository imageRepository;
    @Override
    public ImageDto save(ImageDto image) {
        Image imageSave = imageHelper.saveImage(image);

        return modelMapper.map(imageSave, ImageDto.class);
    }

    @Override
    public void deleteById(Integer imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        image.ifPresent(imageRepository::delete);
    }

    /**
     * @param imageId
     * @return
     */
    @Override
    public ImageDto findById(Integer imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        return image.map(value -> modelMapper.map(value, ImageDto.class)).orElse(null);
    }



}
