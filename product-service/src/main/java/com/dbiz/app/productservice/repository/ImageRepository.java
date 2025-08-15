package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository  extends JpaRepository<Image, Integer> {

    Image save(Image image);

   Image findByImageCode(String Imagecode);
}
