package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductionRepository extends JpaRepository<Production, Integer>, JpaSpecificationExecutor<Production> {
    @Query(value = "SELECT coalesce(MAX(p.id),999999)  FROM Production p")
    Integer getMaxId();
}