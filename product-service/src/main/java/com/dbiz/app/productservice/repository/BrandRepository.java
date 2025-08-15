package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
