package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PromotionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

public interface PromotionTimeRepository extends JpaRepository<PromotionTime, Integer>, JpaSpecificationExecutor<PromotionTime> {

    boolean existsById(Integer promotionTimeId);

    List<PromotionTime> findByPromotionId(Integer promotionId);

    @Query("select distinct u.promotionId from PromotionTime u where u.fromHour <= ?1 and u.toHour >= ?1 and u.isActive = ?2")
    List<Integer> findPromotionIdByHour(Time hour, String isActive);

    @Modifying
    @Query("DELETE FROM PromotionTime pt WHERE pt.promotionId = :promotionId")
    void deleteByPromotionId(@Param("promotionId") Integer promotionId);

}