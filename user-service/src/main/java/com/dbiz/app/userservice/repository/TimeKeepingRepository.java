package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.TimeKeeping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimeKeepingRepository extends JpaRepository<TimeKeeping, Integer>, JpaSpecificationExecutor<TimeKeeping> {
}