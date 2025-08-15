package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.TenantBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TenantBannerRepository extends JpaRepository<TenantBanner, Integer>, JpaSpecificationExecutor<TenantBanner> {

    TenantBanner findByTenantId(Integer tenantId);
}