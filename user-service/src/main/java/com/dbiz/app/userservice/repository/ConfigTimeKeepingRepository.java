package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.ConfigTimeKeeping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConfigTimeKeepingRepository extends JpaRepository<ConfigTimeKeeping, Integer>, JpaSpecificationExecutor<ConfigTimeKeeping> {
}