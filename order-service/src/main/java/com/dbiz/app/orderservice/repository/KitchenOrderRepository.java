package com.dbiz.app.orderservice.repository;


import com.dbiz.app.orderservice.domain.KitchenOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Integer>, JpaSpecificationExecutor<KitchenOrder> {
    @Query(value= "select dko.* from pos.d_kitchen_order dko join pos.d_kitchen_orderline dkol on dko.d_kitchen_order_id = dkol.d_kitchen_order_id \n" +
            "    where dkol.orderline_status in (:orderLineStatus) and dko.d_tenant_id = :tenantId and dko.d_org_id = :orgId  group by dko.d_kitchen_order_id " , nativeQuery = true)
    Page<KitchenOrder>  findKitchenByOrderLineStatus (@Param("orderLineStatus") String[] status, @Param("tenantId") Integer tenantId, @Param("orgId") Integer orgId
            , final Pageable pageable);

    List<KitchenOrder> findByIdIn(List<Integer> id);

    @Query(value = "select p.id from KitchenOrder p where p.posOrderId = :posOrderId")
    Integer findIDBykitchenOrderId (@Param("posOrderId") Integer posOrderId);

    KitchenOrder findByPosOrderId(Integer posOrderId);


    @Query("select coalesce(max(ko.id),1000000) from KitchenOrder ko")
    Integer getMaxKitchenOrderId();

}