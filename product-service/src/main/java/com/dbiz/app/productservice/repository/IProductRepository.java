package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.IProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IProductRepository extends JpaRepository<IProduct, Integer>, JpaSpecificationExecutor<IProduct> {
}