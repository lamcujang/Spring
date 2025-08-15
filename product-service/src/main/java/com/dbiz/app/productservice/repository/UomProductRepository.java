package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.UomProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UomProductRepository extends JpaRepository<UomProduct, Integer>, JpaSpecificationExecutor<UomProduct> {
}