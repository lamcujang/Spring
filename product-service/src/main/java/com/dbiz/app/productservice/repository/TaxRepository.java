package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

public interface TaxRepository extends JpaRepository<Tax, Integer>, JpaSpecificationExecutor<Tax> {
    
    Optional<Tax> findByErpTaxId(Integer erpTaxId);
    Optional<Tax>findByNameAndTenantId(String name,Integer tenantId);

    Optional<Tax> findByNameIgnoreCase(String name);

    Tax findByTenantIdAndTaxRate(Integer tenantId,BigDecimal taxRate);

    Tax findByTaxRate(BigDecimal taxRate);

    Optional<Tax> findFirstByIsDefault(String isDefault);

    @Transactional
    @Modifying
    @Query("update Tax e set e.isDefault = ?1 where e.isDefault = ?2")
    void updateAllIsDefaultById(String isDefault, String value);
}