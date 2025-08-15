package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.view.ProductERequestV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductERequestVRepository extends JpaRepository<ProductERequestV, Integer>, JpaSpecificationExecutor<ProductERequestV> {
}