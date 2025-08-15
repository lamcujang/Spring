package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.TableUseRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface TableUseRefRepository extends JpaRepository<TableUseRef, BigDecimal>, JpaSpecificationExecutor<TableUseRef> {
}