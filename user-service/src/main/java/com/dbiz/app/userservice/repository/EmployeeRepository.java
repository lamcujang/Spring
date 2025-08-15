package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

    Integer countByDepartmentId(Integer departmentId);

    @Query("SELECT coalesce(MAX(p.id),1000000) FROM Employee p")
    Integer getMaxEmployeeId();
}