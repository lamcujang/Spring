package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PromoAssignOrg;
import com.dbiz.app.orderservice.domain.PromoBpGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PromoAssignOrgRepository extends JpaRepository<PromoAssignOrg, Integer>, JpaSpecificationExecutor<PromoAssignOrg> {
    @Query("update PromoAssignOrg u set u.isActive = ?2 where u.promotionId = ?1 " )
    @Modifying
    void updateAllByPromotionId(final Integer promotionId, final String isActive );

    Optional<PromoAssignOrg> findByPromotionIdAndOrgId(final Integer promotionId, final Integer orgId);

    List<PromoAssignOrg> findByPromotionId(Integer promotionId);

    @Query("select u.promotionId from PromoAssignOrg u where u.orgId = ?1 and u.isActive = ?2")
    List<Integer> findPromotionIdByOrgIdAndIsActive(final Integer orgId,String isActive);

    @Query("update from PromoAssignOrg u set  u.isActive = ?2 where u.id = ?1 ")
    @Modifying
    void updateIsActiveById(final Integer id, final String isActive);


    @Query("select u.promotionId from PromoAssignOrg u where u.orgId = ?1 and u.isActive = ?2")
    Set<Integer> findPromotionIdByOrgIdAndIsActiveReponseSet(final Integer orgId, String isActive);
}