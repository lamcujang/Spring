package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PurchaseOrder;
import com.dbiz.app.orderservice.domain.ReturnOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReturnOrderRepository extends JpaRepository<ReturnOrder, Integer> {
    @Query(value = "select coalesce(max(p.id),1000000) from ReturnOrder p ")
    Integer getMaxId();
}