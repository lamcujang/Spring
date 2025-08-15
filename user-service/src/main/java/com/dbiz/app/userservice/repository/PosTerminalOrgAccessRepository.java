package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.PosTerminalOrgAccess;
import com.dbiz.app.userservice.domain.PrimaryPosTerminalOrgAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PosTerminalOrgAccessRepository extends JpaRepository<PosTerminalOrgAccess, PrimaryPosTerminalOrgAccess>, JpaSpecificationExecutor<PosTerminalOrgAccess> {

    List<PosTerminalOrgAccess> findAllByUserIdAndOrgId (final Integer userId, final Integer orgId);

    PosTerminalOrgAccess findByUserIdAndOrgIdAndPosTerminalId (final Integer userId, final Integer orgId, final Integer posTerminalId);
}