package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.EmployeeGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface EmployeeGradeRepository extends JpaRepository<EmployeeGrade, Integer>, JpaSpecificationExecutor<EmployeeGrade> {

    @Query("SELECT coalesce(MAX(p.id),1000000) FROM EmployeeGrade p")
    Integer getMaxEmployeeGradeId();

}
