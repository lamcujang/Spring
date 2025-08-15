package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    @Query(value = "select coalesce(max(p.id),1000000) from PurchaseOrder p ")
    Integer getMaxId();
}