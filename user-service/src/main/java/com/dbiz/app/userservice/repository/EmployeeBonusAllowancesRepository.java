package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.EmployeeBonusAllowances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeBonusAllowancesRepository extends JpaRepository<EmployeeBonusAllowances, Integer> , JpaSpecificationExecutor<EmployeeBonusAllowances> {
}
