package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Integer>, JpaSpecificationExecutor<ExpenseType> {
}
