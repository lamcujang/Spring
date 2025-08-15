package com.dbiz.app.systemservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.systemservice.domain.ReferenceList;
import org.common.dbiz.request.systemRequest.ReferenceListQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class ReferenceListSpecification {

    public static Specification<ReferenceList> hasReferenceId(Integer referenceId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("referenceId"), referenceId);
    }
    public static Specification<ReferenceList> hasValue(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("value"), value);
    }
    public static Specification<ReferenceList> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<ReferenceList> getEntitySpecification(ReferenceListQueryRequest queryRequest) {
        Specification<ReferenceList> spec = Specification.where(null);
        if(queryRequest.getReferenceId() != null)
            spec = spec.and(hasReferenceId(queryRequest.getReferenceId()));
        if(queryRequest.getValue() != null)
            spec = spec.and(hasValue(queryRequest.getValue()));
        spec = spec.and(hasTenantId());
        return spec;
    }
}
