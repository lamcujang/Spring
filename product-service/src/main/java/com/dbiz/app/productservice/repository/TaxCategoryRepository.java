package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.TaxCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TaxCategoryRepository extends JpaRepository<TaxCategory, Integer>, JpaSpecificationExecutor<TaxCategory> {
    Optional<TaxCategory> findByErpTaxCategoryId(Integer erpTaxCategoryId);

    Optional<TaxCategory> findByNameIgnoreCase(String name);
}