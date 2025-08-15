package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Icon;
import com.dbiz.app.productservice.domain.Locator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IconRepository extends JpaRepository<Icon, Integer> {

    List<Icon> findAllIconsByImageUrl(String imageUrl);
}
