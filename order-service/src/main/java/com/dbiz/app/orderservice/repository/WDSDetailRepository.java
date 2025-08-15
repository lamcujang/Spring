package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.WDSDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WDSDetailRepository extends JpaRepository<WDSDetail, Integer> {

    @Query("select w from WDSDetail w where w.statusValue in (:status) and w.kitchenOrderId = :kitchenOrderId")
    List<WDSDetail> findWDSDetails(String[] status, Integer kitchenOrderId);
}