package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {

    @Query(value = "SELECT coalesce(MAX(d_promotion_id),1000000) FROM d_promotion", nativeQuery = true)
    Integer getMaxPromotionId();

    Boolean existsByCode(String code);

    Boolean existsByCodeAndIdNot(String code, Integer id);
}