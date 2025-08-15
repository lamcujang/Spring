package com.dbiz.app.integrationservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class ErpIntegrationSpecification {
    public static Specification<ErpIntegration> hasIsDefault(String isDefault) {
        return (root, query, criteriaBuilder) ->isDefault == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isDefault"), isDefault);
    }

    public static Specification<ErpIntegration> hasType(String type) {
        return (root, query, criteriaBuilder) ->(type == null || type.isEmpty()) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("integrationType"), type);
    }
    
    public static Specification<ErpIntegration> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<ErpIntegration> getErpIntegrationSpecification(ErpIntegrationQueryRequest ErpIntegrationQueryRequest) {
        Specification<ErpIntegration> spec = Specification.where(null);
        spec = spec.and(hasIsDefault(ErpIntegrationQueryRequest.getIsDefault()));
        spec = spec.and(hasTenantId());
        spec=spec.and(hasType(ErpIntegrationQueryRequest.getIntegrationType()));
        return spec;
    }
}
