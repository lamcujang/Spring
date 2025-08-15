package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Uom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UomRepository extends JpaRepository<Uom, Integer>, JpaSpecificationExecutor<Uom> {
    Page<Uom> findAll(Specification specification, Pageable pageable);
    Optional<Uom> findByNameAndTenantId (String name,Integer tenantId);

    Optional<Uom> findByCodeAndTenantId (String code,Integer tenantId);

    Optional<Uom> findByName (String name);

    Optional<Uom> findByErpUomId (Integer erpId);

    Optional<Uom> findByNameIgnoreCaseAndCodeIgnoreCase(String name,String code);
}
