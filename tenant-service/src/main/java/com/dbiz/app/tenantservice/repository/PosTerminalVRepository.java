package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.view.PosTerminalV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PosTerminalVRepository extends JpaRepository<PosTerminalV, Integer>, JpaSpecificationExecutor<PosTerminalV> {
}