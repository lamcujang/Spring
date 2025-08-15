package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductionLineRepository extends JpaRepository<ProductionLine, Integer>, JpaSpecificationExecutor<ProductionLine> {

    void deleteAllByProductionId(Integer productionId);
}