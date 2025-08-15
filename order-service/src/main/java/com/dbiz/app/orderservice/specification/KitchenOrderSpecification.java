package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.KitchenOrder;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.springframework.data.jpa.domain.Specification;

public class KitchenOrderSpecification {
    public static Specification<KitchenOrder> equalWarehouseId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("warehouseId"),  keyword );
    }
    public static Specification<KitchenOrder> equalFloorId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("floorId"),  keyword);
    }
    public static Specification<KitchenOrder> equalTable(Integer keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tableId"), keyword);
    }

//    public static Specification<KitchenOrder> hasOrderStatus(String[] keyword) {
////     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
//        List<String> statusList = Arrays.asList(keyword);
////        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("orderStatus"), statusList);
//        return (root, query, criteriaBuilder) -> root.get("orderStatus").in(statusList);
//    }

    public static Specification<KitchenOrder> hasDocumentno(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("documentno"), keyword);
    }

    public static Specification<KitchenOrder> hasIds(Integer[] ids)
    {
        return (root, query, criteriaBuilder) -> root.get("id").in(ids);
    }


    public static Specification<KitchenOrder> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<KitchenOrder> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<KitchenOrder> getEntity(KitchenOrderRequest request) {
        Specification<KitchenOrder> spec = Specification.where(null);

        if(request.getWarehouseId() != null){
            spec= spec.and(equalWarehouseId(request.getWarehouseId()));
        }
        if(request.getFloorId() != null){
            spec= spec.and(equalFloorId(request.getFloorId()));
        }
        if(request.getTableId()!= null)
        {
            spec= spec.and(equalTable(request.getTableId()));
        }
//        if(request.getOrderStatus()!= null)
//        {
//            spec= spec.and(hasOrderStatus(request.getOrderStatus()));
//        }
        if(request.getDocumentno()!= null)
        {
            spec= spec.and(hasDocumentno(request.getDocumentno()));
        }
        if(request.getOrgId()!= null && request.getOrgId() != 0)
        {
            spec= spec.and(hasOrgId(request.getOrgId() ));
        }
        if(request.getIds()!= null)
        {
            spec= spec.and(hasIds(request.getIds()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
