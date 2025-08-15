package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.BusinessSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.Optional;

public interface BusinessSectorRepository extends JpaRepository<BusinessSector, Integer>, JpaSpecificationExecutor<BusinessSector> {

}