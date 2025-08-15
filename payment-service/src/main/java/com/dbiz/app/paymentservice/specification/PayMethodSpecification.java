package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.paymentservice.domain.PayMethod;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class PayMethodSpecification {
    public static Specification<PayMethod> equalId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), keyword);
    }

    private static Specification<PayMethod> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }
    private static Specification<PayMethod> hasOrgId(Integer orgId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<PayMethod> getSpecification(PayMethodQueryRequest request) {
        Specification<PayMethod> spec = Specification.where(null);
        if(request.getId() != null)
        {
            spec = spec.and(equalId(request.getId()));
        }
        if (request.getOrgId() != null)
        {
            spec = spec.and(hasOrgId(request.getOrgId()));
        }

        spec = spec.and(tenantId(AuditContext.getAuditInfo().getTenantId()));
        return spec;
    }
}