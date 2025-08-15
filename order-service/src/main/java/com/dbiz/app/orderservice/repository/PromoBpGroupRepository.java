package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PromoBpGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PromoBpGroupRepository extends JpaRepository<PromoBpGroup, Integer>, JpaSpecificationExecutor<PromoBpGroup> {

    @Query("update PromoBpGroup u set u.isActive = ?2 where u.promotionId = ?1 " )
    @Modifying
    @Transactional
    void updateAllByPromotionId(final Integer promotionId, final String isActive );

    Optional<PromoBpGroup> findByPromotionIdAndBpGroupId(final Integer promotionId, final Integer bpGroupId);

    List<PromoBpGroup> findByPromotionId(Integer promotionId);


    @Query("update from PromoBpGroup u  set  u.isActive = ?2 where u.id = ?1")
    @Modifying
    void updateIsActiveById(final Integer id, final String isActive);

    @Query("select u.promotionId from PromoBpGroup u where u.bpGroupId = ?1 and u.isActive = ?2")
    Set<Integer> findPromotionIdByBpGroupIdAndIsActive(final Integer bpGroupId, String isActive);
}