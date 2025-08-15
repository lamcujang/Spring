package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.EmployeeBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface EmployeeBankRepository extends JpaRepository<EmployeeBank, Integer>, JpaSpecificationExecutor<EmployeeBank> {
}