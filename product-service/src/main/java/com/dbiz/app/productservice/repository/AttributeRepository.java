package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Integer>, JpaSpecificationExecutor<Attribute> {

    @Query("SELECT a FROM Attribute a WHERE a.code = :code AND a.tenantId = :tenantId")
    Optional<Attribute> findByCodeAndTenantId(String code,Integer tenantId);

}