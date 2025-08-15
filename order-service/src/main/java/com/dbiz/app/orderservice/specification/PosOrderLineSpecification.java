package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.PosOrderline;
import org.common.dbiz.request.orderRequest.PosOrderLineQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class PosOrderLineSpecification {
//    public static Specification<PosOrderline> hasFlooNoLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("floorNo"), "%" + keyword + "%");
//    }
//    public static Specification<PosOrderline> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
//    }
public static Specification<PosOrderline> equalProductId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productId"),   keyword );
    }

    public static Specification<PosOrderline> equalOrderId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("posOrderId"),   keyword );
    }
    public static Specification<PosOrderline> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }
    public static Specification<PosOrderline> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<PosOrderline> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }


    public static Specification<PosOrderline> notCancel() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("cancelReasonId"));
    }
    public static Specification<PosOrderline> getEntitySpecification(PosOrderLineQueryRequest entityQueryRequest) {
        Specification<PosOrderline> spec = Specification.where(null);

        if(entityQueryRequest.getOrderId() != null){
            spec= spec.and(equalOrderId(entityQueryRequest.getOrderId()));
        }
        if(entityQueryRequest.getProductId() != null){
            spec= spec.and(equalProductId(entityQueryRequest.getProductId()));
        }
        if(entityQueryRequest.getOrgId()!= null){
            spec= spec.and(hasOrgId(entityQueryRequest.getOrgId()));
        }
        spec = spec.and(equaIsActive(entityQueryRequest.getIsActive() != null ? entityQueryRequest.getIsActive() : "Y"));

        spec = spec.and(hasTenantId());
        return spec;
    }
}
