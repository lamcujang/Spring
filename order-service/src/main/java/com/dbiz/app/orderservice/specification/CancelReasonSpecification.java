package com.dbiz.app.orderservice.specification;
import com.dbiz.app.orderservice.domain.ReturnReason;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.CancelReason;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class CancelReasonSpecification {

    public static Specification<CancelReason> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }
    public static Specification<CancelReason> hasDescriptionLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
    }
    public static Specification<CancelReason> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<CancelReason> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<CancelReason> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<CancelReason> getCancelReasonSpecification(CancelReasonQueryRequest cancelReasonQueryRequest) {
        Specification<CancelReason> spec = Specification.where(isActive("Y"));

        if(cancelReasonQueryRequest.getName() != null){
            spec= spec.and(hasNameLike(cancelReasonQueryRequest.getName()));
        }
        if(cancelReasonQueryRequest.getDescription() != null){
            spec= spec.and(hasNameLike(cancelReasonQueryRequest.getDescription()));
        }
        if(cancelReasonQueryRequest.getOrgId()!= null)
        {
            spec= spec.and(hasOrgId(cancelReasonQueryRequest.getOrgId()));
        }

        spec = spec.and(hasTenantId());
        return spec;
    }
}
