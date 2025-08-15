package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.TimeSheetSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSheetSummaryRepository extends JpaRepository<TimeSheetSummary, Integer>, JpaSpecificationExecutor<TimeSheetSummary> {
}
