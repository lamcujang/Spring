package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.PcTerminalAccess;
import org.common.dbiz.request.productRequest.PcTerminalAccessQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class PcTerminalAccessSpecification {

    public static Specification<PcTerminalAccess> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<PcTerminalAccess> getEntitySpecification(PcTerminalAccessQueryRequest entityQueryRequest) {
        Specification<PcTerminalAccess> spec = Specification.where(null);
        spec = spec.and(hasTenantId());
        return spec;
    }
}
