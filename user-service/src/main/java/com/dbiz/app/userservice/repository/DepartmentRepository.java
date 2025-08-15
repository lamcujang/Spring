package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
    @Query("SELECT coalesce(MAX(p.id),1000000) FROM Department p")
    Integer getMaxDepartmentID();
}