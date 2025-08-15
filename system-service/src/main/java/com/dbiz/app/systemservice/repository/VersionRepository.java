package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version, Integer>, JpaSpecificationExecutor<Version> {
}
