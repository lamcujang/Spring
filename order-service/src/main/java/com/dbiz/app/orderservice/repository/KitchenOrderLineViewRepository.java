package com.dbiz.app.orderservice.repository;



import com.dbiz.app.orderservice.domain.view.KitchenOrderLineView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KitchenOrderLineViewRepository extends JpaRepository<KitchenOrderLineView, Integer>, JpaSpecificationExecutor<KitchenOrderLineView> {

    @Query("select k from KitchenOrderLineView k where k.kitchenOrderId = :kitchenOrderId AND (\n" +
            "        (:parentId IS NOT NULL AND k.parentId = :parentId)\n" +
            "        OR (:parentId IS NULL AND k.parentId IS NULL)\n" +
            "    )")
    List<KitchenOrderLineView> findByKitchenOrderId(Integer kitchenOrderId, Integer parentId);
}