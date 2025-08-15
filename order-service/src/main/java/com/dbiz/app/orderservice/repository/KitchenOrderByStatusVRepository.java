package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.KitchenOrderLine;
import com.dbiz.app.orderservice.domain.view.KitchenOrderLineByStatusV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KitchenOrderByStatusVRepository extends JpaRepository<KitchenOrderLineByStatusV, Integer> , JpaSpecificationExecutor<KitchenOrderLine> {

    @Query("select k from KitchenOrderLineByStatusV k where k.statusValue in (:status) and k.kitchenOrderId = :kitchenOrderId AND (\n" +
            "        (:parentId IS NOT NULL AND k.parentId = :parentId)\n" +
            "        OR (:parentId IS NULL AND k.parentId IS NULL)\n" +
            "    )")
    List<KitchenOrderLineByStatusV>findByStatusValueInAndKitchenOrderId(String[] status, Integer kitchenOrderId, Integer parentId);
}