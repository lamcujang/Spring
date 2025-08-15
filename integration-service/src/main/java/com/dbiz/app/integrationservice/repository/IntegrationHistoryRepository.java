package com.dbiz.app.integrationservice.repository;

import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IntegrationHistoryRepository extends JpaRepository<IntegrationHistory, Integer>, JpaSpecificationExecutor<IntegrationHistory> {

    @Query(value = "SELECT * FROM pos.d_integration_history where int_type = :intType and int_status = 'COM' ORDER BY int_date DESC LIMIT 1", nativeQuery = true)
    IntegrationHistory findLatestIntegrationDate(String intType);

    Optional<IntegrationHistory> findByIntTypeAndIntStatusAndTenantId(String intType, String intStatus, Integer tenantId);

    Optional<IntegrationHistory>  findByIntTypeAndIntStatusAndTenantIdAndIsActive(String intType, String intStatus, Integer tenantId,String isActive);

    @Query("select  i from IntegrationHistory i where i.intType = :intType and i.intStatus = :intStatus")
    List<IntegrationHistory> findByIntTypeAndIntStatus(String intType, String intStatus);

    @Query("select  i from IntegrationHistory i where i.intType = :intType and i.intStatus = :intStatus and i.id not in :id")
    Optional<IntegrationHistory> findByIntTypeAndIntStatusAndIdNotIn(String intType, String intStatus, Integer id);

    @Query("select  i from IntegrationHistory i where    i.id = :id and i.intStatus = :intStatus")
    Optional<IntegrationHistory> findByIdAndIntStatus(Integer id,String intStatus);
}