package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.GetKolSameProductV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GetKolSameProductVRepository extends JpaRepository<GetKolSameProductV, Integer>, JpaSpecificationExecutor<GetKolSameProductV> {

//    @Query(value = "select p from GetKolSameProductV p where p.tenantId = (:tenantId) and p.orgId = (:orgId) and p.id <> (:kitchenOrderLineId) and p.productId = (:productId) and p.orderLineStatus = (:orderlineStatus) ")

    @Query(value="select p\n" +
            "from pos.get_kol_same_product_v p\n" +
            "where p.d_tenant_id =  :tenantId\n" +
            "  and p.d_org_id= :orgId\n" +
            "  and cast(p.d_kitchen_orderline_id as text) <> cast(:kitchenOrderLineId as text) \n" +
            "  and p.d_product_id = :productId\n" +
            "  and p.orderline_status = :orderlineStatus ", nativeQuery = true)
    List<GetKolSameProductV> getAllSameKitchenLine(Integer tenantId, Integer orgId, Integer kitchenOrderLineId, Integer productId, String orderlineStatus);
    List<GetKolSameProductV> findByTenantIdAndOrgIdAndIdNotAndProductIdAndOrderLineStatus(
            Integer tenantId,
            Integer orgId,
            Integer kitchenOrderLineId,
            Integer productId,
            String orderlineStatus);


}