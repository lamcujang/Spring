package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimesheetRepository extends JpaRepository<Timesheet, Integer>, JpaSpecificationExecutor<Timesheet> {
}