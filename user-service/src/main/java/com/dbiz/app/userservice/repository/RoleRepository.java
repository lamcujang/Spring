package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    Role findByName(String name);
    Boolean existsByName(String name);
    Role findByErpRoleId(Integer erpRoleId);
    Optional<Role> findByIdAndTenantId(Integer id, Integer tenantId);
    Page<Role> findAllByTenantId(Integer tenantId, Pageable pageable);
    Page<Role> findAllByIdIn(List<Integer> ids,Pageable pageable);
    Optional<Role> findByCodeAndTenantId(String code, Integer tenantId);
    @Query(value = "SELECT coalesce(MAX(d_role_id),1000000) FROM d_role", nativeQuery = true)
    Integer getMaxId();
}