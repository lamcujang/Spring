package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.PosTerminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PosTerminalRepository extends JpaRepository<PosTerminal, Integer>, JpaSpecificationExecutor<PosTerminal> {

    Optional<PosTerminal> findByErpPosId(Integer erpPosId);

    List<PosTerminal> findByOrgId(Integer orgId);


    Optional<PosTerminal> findByIsDefaultAndTenantId(String isDefault, Integer tenantId);

    Optional<PosTerminal> findByErpPtmName(String ptmName);
}