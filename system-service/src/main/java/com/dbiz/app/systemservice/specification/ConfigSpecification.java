package com.dbiz.app.systemservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.systemservice.domain.Config;
import org.common.dbiz.request.systemRequest.ConfigQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class ConfigSpecification {

    public static Specification<Config> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Config> getEntitySpecification(ConfigQueryRequest queryRequest) {
        Specification<Config> spec = Specification.where(null);

        spec = spec.and(hasTenantId());
        return spec;
    }
}
