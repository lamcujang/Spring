package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    Image save(Image image);

    Image findByImageCode(String imageCode);
}
