package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.BrandPartner;
import com.dbiz.app.tenantservice.domain.Org;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandPartnerRepository extends JpaRepository<BrandPartner, Integer>, JpaSpecificationExecutor<BrandPartner> {

    Optional<BrandPartner> findByCodeAndIsActive(String code, String isActive);
}