package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.RequestOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RequestOrderLineRepository extends JpaRepository<RequestOrderLine, Integer> , JpaSpecificationExecutor<RequestOrderLine> {

    List<RequestOrderLine> findByTenantIdAndRequestOrderId(Integer tenantId, Integer requestOrderId);
}