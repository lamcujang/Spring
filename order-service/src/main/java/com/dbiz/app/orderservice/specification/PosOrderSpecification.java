package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.PosOrder;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.PosOrderQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class PosOrderSpecification {
    public static Specification<PosOrder> hasDocNo(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("documentNo"), "%" + keyword + "%");
    }
    public static Specification<PosOrder> hasCustomerId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerId"),  keyword);
    }
    public static Specification<PosOrder> orderDate(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderDate"), DateHelper.toLocalDate(keyword));
    }
    public static Specification<PosOrder> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<PosOrder> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<PosOrder> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }

    public static Specification<PosOrder> equaStatus(String[] keyword) {
        return (root, query, criteriaBuilder) -> root.get("orderStatus").in(keyword);
    }


    public static Specification<PosOrder> equalTableId(Integer tableId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tableId"), tableId);
    }
    public static Specification<PosOrder> equalId(Integer posOrderId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), posOrderId);
    }

    public static Specification<PosOrder> equalFloorId(Integer floorId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("floorId"), floorId);
    }
    public static Specification<PosOrder> getEntitySpecification(PosOrderQueryRequest QueryRequest) {
        Specification<PosOrder> spec = Specification.where(null);

        if(QueryRequest.getDocumentNo() != null){
            spec= spec.and(hasDocNo(QueryRequest.getDocumentNo()));
        }
        if(QueryRequest.getCustomerId() != null){
            spec= spec.and(hasCustomerId(QueryRequest.getCustomerId()));
        }
        if(QueryRequest.getOrderDate() != null){
            spec= spec.and(orderDate(QueryRequest.getOrderDate()));
        }
        if (QueryRequest.getOrgId()!= null){
            spec= spec.and(hasOrgId(QueryRequest.getOrgId()));
        }
        if(QueryRequest.getOrderStatus() != null){
            spec= spec.and(equaStatus(QueryRequest.getOrderStatus()));
        }
        if(QueryRequest.getFloorId() != null){
            spec= spec.and(equalFloorId(QueryRequest.getFloorId()));
        }
        if(QueryRequest.getTableId() != null){
            spec= spec.and(equalTableId(QueryRequest.getTableId()));
        }
        if(QueryRequest.getPosOrderId()!=null)
            spec = spec.and(equalId(QueryRequest.getPosOrderId()));

        spec = spec.and(equaIsActive(QueryRequest.getIsActive() != null ? QueryRequest.getIsActive() : "Y"));

        spec = spec.and(hasTenantId());
        return spec;
    }
}
