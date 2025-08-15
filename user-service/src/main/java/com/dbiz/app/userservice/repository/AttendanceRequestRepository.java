package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.AttendanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttendanceRequestRepository extends JpaRepository<AttendanceRequest, Integer>, JpaSpecificationExecutor<AttendanceRequest> {
}