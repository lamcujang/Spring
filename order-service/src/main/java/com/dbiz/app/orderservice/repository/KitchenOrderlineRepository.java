package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.KitchenOrderLine;
import org.common.dbiz.dto.orderDto.response.ProductToppingViewKDSDto;
import org.common.dbiz.dto.orderDto.response.ProductToppingViewKDSProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KitchenOrderlineRepository extends JpaRepository<KitchenOrderLine, Integer> , JpaSpecificationExecutor<KitchenOrderLine> {

    @Query(value = "SELECT * FROM pos.d_kitchen_orderline WHERE d_kitchen_order_id = :kitchenOrderId AND orderline_status IN (:status)", nativeQuery = true)
    List<KitchenOrderLine> findAllByKichenOrderIdAndStatus(@Param("kitchenOrderId") Integer kitchenOrderId, @Param("status") List<String> status);


    List<KitchenOrderLine> findAllByPosOrderLineId(Integer posOrderLineId);

    @Query(value = "select p.productionId  from KitchenOrderLine p where p.tenantId = (:tenantId) " +
            "and p.orgId = (:orgId) and p.orderlineStatus in (:orderLineStatus)   group by  p.productionId,p.kitchenOrderId")
    Integer[] findProductionId (@Param("tenantId") Integer tenantId, @Param("orgId") Integer orgId, @Param("orderLineStatus") String[] orderLineStatus);

    @Query(value = "select p from KitchenOrderLine p where p.tenantId = (:tenantId) " +
            "and p.orgId = (:orgId) and p.productionId in (:productionId) and p.productId in (:productId) ")
    List<KitchenOrderLine> findALlProductIdAndProductionId(@Param("tenantId") Integer tenantId,
                                                           @Param("orgId") Integer orgId,
                                                           @Param("productionId") Integer productionId,
                                                           @Param("productId") Integer productId);

    @Query(value="select p from KitchenOrderLine p where p.tenantId = (:tenantId) and p.orgId = (:orgId) and p.kitchenOrderId <> (:kitchenOrderId) and p.productId = (:productId) and p.orderlineStatus = (:orderlineStatus) ")
    List<KitchenOrderLine> getAllSameKitchenLine(Integer tenantId, Integer orgId, Integer kitchenOrderId, Integer productId, String orderlineStatus);

    @Query(value =
            "SELECT pr.d_product_id AS id, pr.code AS code, pr.name AS name, kol.qty as qty, kol.d_kitchen_orderline_id AS kitchenOrderLineId, kol.parent_id AS parentId " +
                    "FROM pos.d_kitchen_orderline kol " +
                    "INNER JOIN pos.d_product pr ON kol.d_product_id = pr.d_product_id " +
                    "WHERE kol.parent_id = :kitchenOrderId " +
                    "AND kol.is_active = 'Y' " +
                    "AND pr.is_active = 'Y'",
            nativeQuery = true)
    List<ProductToppingViewKDSProjection> findProductToppingByParentId(@Param("kitchenOrderId") Integer kitchenOrderId);

}

