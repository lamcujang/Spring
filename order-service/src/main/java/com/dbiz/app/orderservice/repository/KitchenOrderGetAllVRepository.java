package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.KitchenOrder;
import com.dbiz.app.orderservice.domain.view.KitchenOrderGetAllV;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface KitchenOrderGetAllVRepository extends JpaRepository<KitchenOrderGetAllV, Integer>, JpaSpecificationExecutor<KitchenOrderGetAllV> {

    @Query(value = "select distinct dko from KitchenOrderGetAllV dko join KitchenOrderLine dkol on dko.id = dkol.kitchenOrderId \n" +
            "    where dkol.orderlineStatus in (:orderLineStatus) and dko.tenantId = :tenantId and dko.orgId = :orgId  and (dko.warehouseId = :warehouseId or :warehouseId is null)   " ) // group by dko.id,dko.created,dko.createdBy,dko.updated,dko.updatedBy,dko.tenantId,dko.orgId,dko.isActive")
    Page<KitchenOrderGetAllV> findKitchenByOrderLineStatus(@Param("orderLineStatus") String[] status, @Param("tenantId") Integer tenantId, @Param("orgId") Integer orgId,Integer warehouseId
            , final Pageable pageable);

//    @Query(value= "select dko.* from pos.d_kitchen_order_get_all_v dko join pos.d_kitchen_orderline dkol on dko.d_kitchen_order_id = dkol.d_kitchen_order_id \n" +
//            "    where dkol.orderline_status in (:orderLineStatus) and dko.d_tenant_id = :tenantId and dko.d_org_id = :orgId  group by dko.d_kitchen_order_id " , nativeQuery = true)
//    Page<KitchenOrderGetAllV>  findKitchenByOrderLineStatus (@Param("orderLineStatus") String[] status, @Param("tenantId") Integer tenantId, @Param("orgId") Integer orgId
//            , final Pageable pageable);
}