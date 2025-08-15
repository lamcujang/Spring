package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PromotionMethod;
import com.dbiz.app.orderservice.domain.PromotionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PromotionMethodRepository extends JpaRepository<PromotionMethod, Integer>, JpaSpecificationExecutor<PromotionMethod> {

    List<PromotionMethod> findByPromotionId(Integer promotionId);

    boolean existsById(Integer promotionMethodId);

}