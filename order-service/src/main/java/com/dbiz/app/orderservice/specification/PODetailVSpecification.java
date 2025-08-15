package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.view.PODetailV;
import org.common.dbiz.request.orderRequest.PODetailVRequest;
import org.springframework.data.jpa.domain.Specification;

public class PODetailVSpecification {

    private static Specification<PODetailV> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    private static Specification<PODetailV> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<PODetailV> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    private static Specification<PODetailV> purchaseOrderId(Integer poId) {
        return (root, query, criteriaBuilder) -> poId == null ? null : criteriaBuilder.equal(root.get("purchaseOrderId"), poId);
    }

    public static Specification<PODetailV> getSpecification(PODetailVRequest request) {
        return Specification.where(tenantId(AuditContext.getAuditInfo().getTenantId())
                        .and(orgId(request.getOrgId()))
                        .and(isActive(request.getIsActive()))
                        .and(orgId(request.getOrgId()))
                        .and(purchaseOrderId(request.getPurchaseOrderId())));
    }
}
