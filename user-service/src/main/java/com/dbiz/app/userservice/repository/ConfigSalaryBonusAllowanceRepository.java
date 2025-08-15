package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.ConfigSalaryBonusAllowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigSalaryBonusAllowanceRepository extends JpaRepository<ConfigSalaryBonusAllowance, Integer>, JpaSpecificationExecutor<ConfigSalaryBonusAllowance> {
}
