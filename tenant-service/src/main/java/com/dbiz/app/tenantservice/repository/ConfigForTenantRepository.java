package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.ConfigForTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConfigForTenantRepository extends JpaRepository<ConfigForTenant, Integer> {

  Optional<ConfigForTenant> findByNameAndTenantId(String name, Integer tenantId);

  @Query(value = "select p.value from d_config p where p.name = :name and p.d_tenant_id = :tenantId", nativeQuery = true)
  String findValueByNameAndTenantId(@Param("name") String name , @Param("tenantId") Integer tenantId);
}