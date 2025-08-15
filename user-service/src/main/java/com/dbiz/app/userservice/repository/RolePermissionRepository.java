package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.RolePermission;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer>, JpaSpecificationExecutor<RolePermission> {
    RolePermission findByRoleIdAndTenantId(Integer roleId, Integer tenantId);

    @Transactional
    @Query(value = "UPDATE pos.d_role_permission SET json_data = :jsonData WHERE d_role_id = :roleId AND d_tenant_id = :tenantId", nativeQuery = true)
    void updateRolePermissionById(Integer roleId, Integer tenantId, JsonNode jsonData);
}