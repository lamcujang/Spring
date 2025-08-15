package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.EmployeeContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface EmployeeContactRepository extends JpaRepository<EmployeeContact, Integer>, JpaSpecificationExecutor<EmployeeContact> {
}