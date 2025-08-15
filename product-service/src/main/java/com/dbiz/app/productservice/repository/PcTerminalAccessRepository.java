package com.dbiz.app.productservice.repository;


import com.dbiz.app.productservice.domain.PcTerminalAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PcTerminalAccessRepository extends JpaRepository<PcTerminalAccess, Integer>, JpaSpecificationExecutor<PcTerminalAccess> {
    PcTerminalAccess  findByProductCategoryIdAndPosTerminalId(Integer productCategoryId, Integer posTerminalId);

    @Transactional
    @Modifying
    @Query("update PcTerminalAccess p set p.isActive = :isActive where p.productCategoryId = :productCategoryId and p.tenantId = :tenantId")
    void updateIsActiveByTenantIdAndProductCategoryId(String isActive, Integer tenantId, Integer productCategoryId);

    PcTerminalAccess findByProductCategoryIdAndOrgId(Integer productCategoryId, Integer orgId);

    List<PcTerminalAccess> findAllByProductCategoryId(Integer productCategoryId);

    PcTerminalAccess findByProductCategoryIdAndOrgIdAndPosTerminalId(Integer productCategoryId, Integer orgId, Integer posTerminalId);

    @Transactional
    @Modifying
    void deleteAllByProductCategoryId(Integer productCategoryId);
}