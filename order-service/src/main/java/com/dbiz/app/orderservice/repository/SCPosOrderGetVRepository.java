package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.SCPosOrderGetV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SCPosOrderGetVRepository extends JpaRepository<SCPosOrderGetV, Integer>, JpaSpecificationExecutor<SCPosOrderGetV> {
    SCPosOrderGetV findByShiftControlId(Integer shiftControlId);
}