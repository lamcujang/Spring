package com.dbiz.app.integrationservice.repository;

import com.dbiz.app.integrationservice.domain.ErpIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;

public interface ErpIntegrationRepository extends JpaRepository<ErpIntegration, Integer>, JpaSpecificationExecutor<ErpIntegration> {
    ErpIntegration findByErpUrl(String url);

    ErpIntegration findByIsDefault(String isDefault);

    ErpIntegration findByIsActiveAndIsDefault(String isActive, String isDefault);

    @Transactional
    @Modifying
    @Query("update ErpIntegration e set e.isDefault = ?1 where e.tenantId = ?2 and e.isActive = ?3")
    void updateAllIsDefaultById(String isDefault, Integer id, String isActive);


    ErpIntegration findFirstByErpPlatformAndIsDefaultAndIsActive(String erpPlatform, String isDefault, String isActive);


}