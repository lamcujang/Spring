package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.SalaryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SalaryConfigRepository extends JpaRepository<SalaryConfig, Integer>, JpaSpecificationExecutor<SalaryConfig> {
}