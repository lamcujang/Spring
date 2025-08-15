package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.ConfigShiftEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ConfigShiftEmployeeRepository extends JpaRepository<ConfigShiftEmployee, Integer>, JpaSpecificationExecutor<ConfigShiftEmployee> {

    boolean existsByEmployeeIdAndConfigShiftId(Integer employeeId, Integer configShiftId);
    ConfigShiftEmployee findByEmployeeIdAndConfigShiftId(Integer employeeId, Integer configShiftId);

    @Modifying
    @Transactional
    @Query("UPDATE ConfigShiftEmployee s SET s.isActive = :isActive WHERE s.configShiftId = :shiftId")
    int updateIsActiveByShiftId(@Param("isActive") String isActive, @Param("shiftId") Integer shiftId);


}