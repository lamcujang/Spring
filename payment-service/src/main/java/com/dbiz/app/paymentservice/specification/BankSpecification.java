package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.paymentservice.domain.Bank;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.paymentRequest.BankQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class BankSpecification {
    public static Specification<Bank> likeName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    private static Specification<Bank> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<Bank> equalId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Bank> getSpecification(BankQueryRequest request) {
        Specification<Bank> spec = Specification.where(null);
        if (request.getKeyword() != null) {
            spec = spec.or(likeName(request
                    .getKeyword()));
        }
        spec = spec.and(equalId(request.getId()));
        spec = spec.and(tenantId(AuditContext.getAuditInfo().getTenantId()));
        return spec;
    }
}