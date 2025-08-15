package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.WDSHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WDSHeaderRepository extends JpaRepository<WDSHeader, Integer> {

    @Query(value = "select distinct wds from WDSHeader wds" +
            "    join KitchenOrderLine dkol on wds.id = dkol.kitchenOrderId " +
            "    where dkol.orderlineStatus in (:orderStatus) " +
            "    and wds.orgId = :orgId  and (wds.warehouseId = :warehouseId or :warehouseId is null)   " ) // group by dko.id,dko.created,dko.createdBy,dko.updated,dko.updatedBy,dko.tenantId,dko.orgId,dko.isActive")
    Page<WDSHeader> findWDSHeaders(@Param("orderStatus") String[] status
            , @Param("orgId") Integer orgId, Integer warehouseId
            , final Pageable pageable);
}