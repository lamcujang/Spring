package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, Integer> {
    @Query("SELECT p FROM PurchaseOrderLine p WHERE p.purchaseOrderId = :purcharOrder")
    List<PurchaseOrderLine> findAllByPurchaseOrder(Integer purcharOrder);
}