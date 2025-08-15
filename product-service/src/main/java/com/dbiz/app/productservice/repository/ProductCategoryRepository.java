package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>, JpaSpecificationExecutor<ProductCategory> {

    Page<ProductCategory> findAll(Specification specification, Pageable pageable);
    @Query("SELECT coalesce(max (p.id),1000000) FROM ProductCategory p ")
    Integer getMaxId();

    ProductCategory findByErpProductCategoryId(Integer erpProductCategoryId);

    @Query("SELECT p FROM ProductCategory p WHERE p.erpProductCategoryId = :erpProductCategoryId")
    List<ProductCategory> findByErpProductCategoryIds(Integer erpProductCategoryId);

    ProductCategory findByCode(String code);

    ProductCategory findByName(String name);
}
