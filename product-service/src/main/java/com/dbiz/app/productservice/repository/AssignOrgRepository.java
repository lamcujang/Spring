package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.AssignOrgProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface AssignOrgRepository extends JpaRepository<AssignOrgProduct, Integer>, JpaSpecificationExecutor<AssignOrgProduct> {
    @Modifying
    @Transactional
    @Query(value = "delete from AssignOrgProduct a where a.productId = ?1")
    void deleteAllByProductId(Integer productId);

    @Modifying
    @Transactional
    @Query(value = "update AssignOrgProduct a set a.isActive = 'N' where a.productId = ?1")
    void updateIsActiveAllByProductId(Integer productId);

    Optional<AssignOrgProduct> getByOrgIdAndProductId(Integer orgId, Integer productId);

}