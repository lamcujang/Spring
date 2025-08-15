package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.Org;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrgRepository extends JpaRepository<Org, Integer>, JpaSpecificationExecutor<Org> {
    Page<Org> findAll(Specification specification, Pageable pageable);

    @Query("select coalesce(max(o.id),1000000) from Org o")
    Integer getMaxId();

    Optional<Org> findByErpOrgId(Integer erpOrgId);

    List<Org> findByErpOrgIdAndTenantId(Integer erpOrgId, Integer tenantId);
    Optional<Org> findByErpOrgName(String orgName);

}