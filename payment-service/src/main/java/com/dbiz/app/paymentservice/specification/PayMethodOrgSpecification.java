package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.paymentservice.domain.PayMethodOrg;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.paymentRequest.PayMethodOrgQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class PayMethodOrgSpecification {
    public static Specification<PayMethodOrg> equalId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), keyword);
    }

    private static Specification<PayMethodOrg> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<PayMethodOrg> hasOrgId(Integer orgId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }
    public static Specification<PayMethodOrg> getSpecification(PayMethodOrgQueryRequest request) {
        Specification<PayMethodOrg> spec = Specification.where(null);
        if(request.getId() != null)
        {
            spec = spec.and(equalId(request.getId()));
        }
        if(request.getOrgId()!= null)
        {
            spec = spec.and(hasOrgId(request.getOrgId()));
        }
        spec = spec.and(tenantId(AuditContext.getAuditInfo().getTenantId()));
        return spec;
    }
}