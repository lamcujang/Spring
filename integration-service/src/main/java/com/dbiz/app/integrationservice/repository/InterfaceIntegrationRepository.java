package com.dbiz.app.integrationservice.repository;

import com.dbiz.app.integrationservice.domain.InterfaceIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InterfaceIntegrationRepository extends JpaRepository<InterfaceIntegration, Integer>, JpaSpecificationExecutor<InterfaceIntegration> {
    List<InterfaceIntegration> findByTypeAndStatus(String type, String status);

    List<InterfaceIntegration> findByTypeInAndStatus(List<String> types, String status);
}