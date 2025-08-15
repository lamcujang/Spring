package com.dbiz.app.productservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.Tax;
import org.common.dbiz.request.productRequest.TaxQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxSpecification {
    public static Specification<Tax> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<Tax> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }
    public static Specification<Tax> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Tax> equaOrgId(Integer orgId ) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<Tax> equalIsDefault(String isDefault ) {
        return (root, query, criteriaBuilder) ->isDefault == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("isDefault"), isDefault);
    }
    public static Specification<Tax> getEntitySpecification(TaxQueryRequest entityQueryRequest) {
        Specification<Tax> spec = Specification.where(null);
        if(entityQueryRequest.getName() != null){
            spec = spec.and(hasNameLike(entityQueryRequest
                    .getName()));
        }

        if(entityQueryRequest.getIsActive()!=null)
        {
            spec= spec.and(hasIsActive(entityQueryRequest.getIsActive()));
        }
        if(entityQueryRequest.getOrgId() != null && entityQueryRequest.getOrgId() != 0){
            spec= spec.and(equaOrgId(entityQueryRequest.getOrgId()));
        }
        spec = spec.and(hasTenantId());
        spec = spec.and(equalIsDefault(entityQueryRequest.getIsDefault()));
        return spec;
    }
}
