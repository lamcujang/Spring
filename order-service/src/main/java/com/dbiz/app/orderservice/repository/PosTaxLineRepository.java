package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PosTaxLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PosTaxLineRepository extends JpaRepository<PosTaxLine, Integer>, JpaSpecificationExecutor<PosTaxLine> {

    @Modifying
    @Query(value = "update PosTaxLine  a set a.isActive = ?1 where a.posOrderId = ?2")
    void updateStatusPosTaxLines(String isActive,Integer posOrderId);


    PosTaxLine findByPosOrderIdAndTaxId(Integer posOrderId, Integer taxId);
}