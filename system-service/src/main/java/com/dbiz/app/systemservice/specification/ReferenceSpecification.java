package com.dbiz.app.systemservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.systemservice.domain.Reference;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class ReferenceSpecification {

    public Specification<Reference> hasName(String name)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }
    public static Specification<Reference> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Reference> getEntitySpecification(ReferenceQueryRequest queryRequest) {
        Specification<Reference> spec = Specification.where(null);

        if(queryRequest.getName() != null) {
            spec = spec.and(new ReferenceSpecification().hasName(queryRequest.getName()));
        }

        spec = spec.and(hasTenantId());
        return spec;
    }
}
