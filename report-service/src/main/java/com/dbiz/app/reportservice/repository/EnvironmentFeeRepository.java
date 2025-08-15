package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.EnvironmentFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnvironmentFeeRepository extends JpaRepository<EnvironmentFee, Integer>, JpaSpecificationExecutor<EnvironmentFee> {

    List<EnvironmentFee> findAllByTaxType(Integer productParentId);

    EnvironmentFee findByItemCode(String code);

    List<EnvironmentFee> findByItemName(String name);

}
