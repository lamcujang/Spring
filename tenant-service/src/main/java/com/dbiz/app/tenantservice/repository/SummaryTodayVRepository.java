package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.SummaryTodayV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SummaryTodayVRepository extends JpaRepository<SummaryTodayV, Integer>, JpaSpecificationExecutor<SummaryTodayV> {
}