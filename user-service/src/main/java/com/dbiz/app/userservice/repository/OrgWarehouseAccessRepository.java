package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.OrgWarehouseAccess;
import com.dbiz.app.userservice.domain.PrimaryOrgWarehouseAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface OrgWarehouseAccessRepository extends JpaRepository<OrgWarehouseAccess, PrimaryOrgWarehouseAccess>, JpaSpecificationExecutor<OrgWarehouseAccess> {

    @Transactional
    @Modifying
    @Query("update OrgWarehouseAccess u set u.isActive = ?3 where u.userId = ?1 and u.warehouseId = ?2 and u.orgId = ?4 and u.tenantId = ?5")
    void updateIsActive(final Integer userId, final Integer warehouseId, final String isActive, final Integer orgId, final Integer tenantId);

    @Transactional
    @Modifying
    @Query("update OrgWarehouseAccess u set u.isActive = :isActive where u.userId = :userId  and u.tenantId = :tenantId")
    void updateIsActive(final Integer userId, final String isActive, final Integer tenantId);



    OrgWarehouseAccess findByUserIdAndWarehouseIdAndOrgIdAndTenantId(final Integer userId, final Integer warehouseId, final Integer orgId, final Integer tenantId);
}