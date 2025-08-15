package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.TaxCategory;
import org.common.dbiz.request.productRequest.TaxCategoryQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxCategorySpecification {
    public static Specification<TaxCategory> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }
    public static Specification<TaxCategory> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<TaxCategory> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }
    public static Specification<TaxCategory> getEntitySpecification(TaxCategoryQueryRequest entityQueryRequest) {
        Specification<TaxCategory> spec = Specification.where(null);
        if(entityQueryRequest.getName() != null){
            spec = spec.and(hasNameLike(entityQueryRequest
                    .getName()));
        }

        if(entityQueryRequest.getOrgId() != null && entityQueryRequest.getOrgId() != 0){
            spec= spec.and(equaOrgId(entityQueryRequest.getOrgId()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
