package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.paymentservice.domain.InvoiceLine;
import org.common.dbiz.request.paymentRequest.InvoiceLineQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class InvoiceLineSpecification {

    public static Specification<InvoiceLine> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<InvoiceLine> byInvoiceId(Integer invoiceId) {
        return (root, query, criteriaBuilder) -> invoiceId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceId"), invoiceId);
    }

    public static Specification<InvoiceLine> byOrderLineId(Integer orderLineId) {
        return (root, query, criteriaBuilder) -> orderLineId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orderLineId"), orderLineId);
    }

    public static Specification<InvoiceLine> byProductId(Integer productId) {
        return (root, query, criteriaBuilder) -> productId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("productId"), productId);
    }

    public static Specification<InvoiceLine> byTaxId(Integer taxId) {
        return (root, query, criteriaBuilder) -> taxId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("taxId"), taxId);
    }

    private static Specification<InvoiceLine> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    private static Specification<InvoiceLine> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<InvoiceLine> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<InvoiceLine> getSpecification(InvoiceLineQueryRequest request) {
        return Specification.where(byId(request.getId()))
                .and(tenantId(AuditContext.getAuditInfo().getTenantId())
                .and(orgId(request.getOrgId()))
                .and(isActive(request.getIsActive()))
                .and(byInvoiceId(request.getInvoiceId()))
                .and(byOrderLineId(request.getOrderLineId()))
                .and(byProductId(request.getProductId()))
                .and(byTaxId(request.getTaxId())));
    }

}
