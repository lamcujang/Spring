package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PurchaseOrderLine;
import com.dbiz.app.orderservice.domain.ReturnOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReturnOrderLineRepository extends JpaRepository<ReturnOrderLine, Integer> {
    List<ReturnOrderLine> findAllByReturnOrderId(Integer ReturnOrderId);
}