package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.ShiftControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShiftControlRepository extends JpaRepository<ShiftControl, Integer>, JpaSpecificationExecutor<ShiftControl> {
    @Query(value = "SELECT coalesce(MAX(id),1000000) FROM ShiftControl ")
    Integer getMaxId();

    Optional<ShiftControl> findById(Integer id);


    @Query(value = "SELECT coalesce(MAX(sequenceNo),0) FROM ShiftControl ")
    Integer getMaxSequenceNo();


    @Query(value = "SELECT coalesce(MAX(sequenceNo),0) FROM ShiftControl  WHERE orgId = ?1 AND posTerminalId = ?2")
    Integer getMaxSequenceNoByOrgAndPosTerminal(Integer orgId, Integer posTerminalId);


    Optional<ShiftControl> findByTenantIdAndOrgIdAndPosTerminalIdAndSequenceNo(Integer tenantId, Integer orgId, Integer posTerminalId, Integer sequenceNo);
}