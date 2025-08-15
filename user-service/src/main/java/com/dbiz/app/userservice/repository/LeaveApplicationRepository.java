package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer>, JpaSpecificationExecutor<LeaveApplication> {
    List<LeaveApplication> findByTimesheetSummaryId(Integer timeSheetId);
}
