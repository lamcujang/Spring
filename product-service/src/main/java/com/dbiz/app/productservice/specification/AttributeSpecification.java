package com.dbiz.app.productservice.specification;

import com.dbiz.app.productservice.domain.Attribute;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class AttributeSpecification {

    public static Specification<Attribute> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Attribute> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<Attribute> getEntitySpecification(AttributeQueryRequest queryRequest) {
        Specification<Attribute> spec = Specification.where(null);
        if(queryRequest.getOrgId() != null){
            spec= spec.and(equaOrgId(queryRequest.getOrgId()));
        }

        spec = spec.and(hasTenantId());
        return spec;
    }
}
