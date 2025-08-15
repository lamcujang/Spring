package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReferenceRepository extends JpaRepository<Reference, Integer>, JpaSpecificationExecutor<Reference> {
}