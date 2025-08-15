package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.EmployeeAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Integer>, JpaSpecificationExecutor<EmployeeAttendance> {
}