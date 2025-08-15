package com.dbiz.app.integrationservice.repository;

import com.dbiz.app.integrationservice.domain.view.IntegrationHistoryGetV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IntegrationHistoryGetVRepository extends JpaRepository<IntegrationHistoryGetV, Integer>, JpaSpecificationExecutor<IntegrationHistoryGetV> {
}