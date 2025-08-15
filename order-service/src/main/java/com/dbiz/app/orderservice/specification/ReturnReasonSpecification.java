package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.CancelReason;
import com.dbiz.app.orderservice.domain.ReturnReason;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.common.dbiz.request.orderRequest.ReturnReasonQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class ReturnReasonSpecification {

    public static Specification<ReturnReason> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }
    public static Specification<ReturnReason> hasDescriptionLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
    }
    public static Specification<ReturnReason> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<ReturnReason> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) ->  criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<ReturnReason> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<ReturnReason> getReturnReasonSpecification(ReturnReasonQueryRequest returnReasonQueryRequest) {
        Specification<ReturnReason> spec = Specification.where(isActive("Y"));

        if(returnReasonQueryRequest.getName() != null){
            spec= spec.and(hasNameLike(returnReasonQueryRequest.getName()));
        }
        if(returnReasonQueryRequest.getDescription() != null){
            spec= spec.and(hasNameLike(returnReasonQueryRequest.getDescription()));
        }
        if(returnReasonQueryRequest.getOrgId()!= null)
        {
            spec= spec.and(hasOrgId(returnReasonQueryRequest.getOrgId()));
        }

        spec = spec.and(hasTenantId());
        return spec;
    }
}
