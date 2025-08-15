package com.dbiz.app.productservice.specification;

import com.dbiz.app.productservice.domain.AttributeValue;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.productRequest.AttributeValueQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class AttributeValueSpecification {

    public static Specification<AttributeValue> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<AttributeValue> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<AttributeValue> getEntitySpecification(AttributeValueQueryRequest queryRequest) {
        Specification<AttributeValue> spec = Specification.where(null);
        if(queryRequest.getOrgId() != null){
            spec= spec.and(equaOrgId(queryRequest.getOrgId()));
        }

        spec = spec.and(hasTenantId());
        return spec;
    }
}
