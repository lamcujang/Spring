package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer>, JpaSpecificationExecutor<OrderLine> {
}