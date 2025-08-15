package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AllowanceRepository extends JpaRepository<Allowance, Integer>, JpaSpecificationExecutor<Allowance> {
}