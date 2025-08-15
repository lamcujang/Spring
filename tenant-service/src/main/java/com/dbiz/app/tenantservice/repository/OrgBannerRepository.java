package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.OrgBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrgBannerRepository extends JpaRepository<OrgBanner, Integer>, JpaSpecificationExecutor<OrgBanner> {
    OrgBanner findByOrOrgId(Integer orgId);
    OrgBanner findFirstByOrgIdAndCode(Integer orgId,String code);
    List<OrgBanner> findAllByOrgIdAndName(Integer orgId, String name);
    List<OrgBanner> findByImageIdAndOrgId(Integer imageId,Integer orgId);

}