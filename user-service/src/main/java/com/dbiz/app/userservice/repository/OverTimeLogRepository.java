package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.LeaveApplication;
import com.dbiz.app.userservice.domain.OverTimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverTimeLogRepository extends JpaRepository<OverTimeLog, Integer>, JpaSpecificationExecutor<OverTimeLog> {
    List<OverTimeLog> findByTimesheetSummaryId(Integer timeSheetId);
}
