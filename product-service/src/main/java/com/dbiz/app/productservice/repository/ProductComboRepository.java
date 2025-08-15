package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.ProductCombo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductComboRepository extends JpaRepository<ProductCombo, Integer>, JpaSpecificationExecutor<ProductCombo> {
    Page<ProductCombo> findAll(Specification<ProductCombo> spec, Pageable pageable);

    ProductCombo findDistinctByProductId(Integer productId);

    List<ProductCombo> findByProductId(Integer productId);

    List<ProductCombo> findByProductIdAndIsItem(Integer productId,String isItem);

    @Modifying
    @Transactional
    @Query(value = "update ProductCombo  a set a.isActive = 'N' where a.productId = ?1 and a.isItem = ?2")
    void updateAllByProductId(Integer productId,String isItem);


    Optional<ProductCombo> findByProductIdAndProductComponentIdAndIsItem(Integer productId, Integer componentId,String isItem);
}