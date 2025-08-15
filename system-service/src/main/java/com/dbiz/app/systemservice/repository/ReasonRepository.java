package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReasonRepository extends JpaRepository<Reason, Integer>, JpaSpecificationExecutor<Reason> {
}